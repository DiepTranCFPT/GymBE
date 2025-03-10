package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.Members;
import com.gymsystem.cyber.entity.SchedulesIO;
import com.gymsystem.cyber.iService.IFaceRecodeService;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.model.Response.FaceReposi;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.AuthenticationRepository;
import com.gymsystem.cyber.repository.MemberRepository;
import com.gymsystem.cyber.repository.ScheduleIORepository;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_features2d.BFMatcher;
import org.bytedeco.opencv.opencv_features2d.ORB;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class FaceRecodeService implements IFaceRecodeService {

    private final AuthenticationRepository authenticationRepository;
    private final CascadeClassifier faceDetector;
    private final ScheduleIORepository scheduleIORepository;
    private final MemberRepository memberRepository;
    private List<User> users;

    @Autowired
    public FaceRecodeService(AuthenticationRepository authenticationRepository,
                             ScheduleIORepository scheduleIORepository,
                             MemberRepository memberRepository) {
        this.authenticationRepository = authenticationRepository;
        this.scheduleIORepository = scheduleIORepository;
        this.memberRepository = memberRepository;

        // Load the face detection model
        users = authenticationRepository.findByAvataIsNotNull();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("haarcascade_frontalface_default.xml");
            if (inputStream == null) {
                throw new IOException("Face detection model file not found!");
            }
            File tempFile = File.createTempFile("cascade_", ".xml");
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            this.faceDetector = new CascadeClassifier(tempFile.getAbsolutePath());
            if (this.faceDetector.empty()) {
                throw new IOException("Failed to load face detection model!");
            }
            tempFile.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize face detection model: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> regisFaceIDforAccount(String email, MultipartFile file) throws IOException {
        User user = authenticationRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return CompletableFuture.completedFuture(new ResponseObject("Account not found!", HttpStatus.BAD_REQUEST, false));
        }

        if (user.getAvata() != null) {
            return CompletableFuture.completedFuture(new ResponseObject("FaceID already exists!", HttpStatus.BAD_REQUEST, false));
        }

        if (user.isDeleted()) {
            return CompletableFuture.completedFuture(new ResponseObject("Account has been deleted!", HttpStatus.BAD_REQUEST, false));
        }

        if (!user.isEnable()) {
            return CompletableFuture.completedFuture(new ResponseObject("Account is not enabled!", HttpStatus.BAD_REQUEST, false));
        }

        File tempFile = File.createTempFile("face_", ".jpg");
        try {
            file.transferTo(tempFile);
            if (!isFaceExist(tempFile)) {
                return CompletableFuture.completedFuture(new ResponseObject("No face detected!", HttpStatus.BAD_REQUEST, false));
            }

            byte[] imageBytes = Files.readAllBytes(tempFile.toPath());
            user.setAvata(imageBytes);
            authenticationRepository.saveAndFlush(user);

            return CompletableFuture.completedFuture(new ResponseObject("FaceID registered successfully", HttpStatus.OK, true));
        } finally {
            Files.deleteIfExists(tempFile.toPath());
        }
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<ResponseObject> loginFaceID(MultipartFile file) throws IOException {
        byte[] imageBytes = file.getBytes();
        Mat inputImage = convertByteArrayToMat(imageBytes);

        if (inputImage.empty()) {
            return CompletableFuture.completedFuture(new ResponseObject("Invalid image!", HttpStatus.BAD_REQUEST, null));
        }

        Mat face = detectAndNormalizeFace(inputImage);
        if (face == null) {
            return CompletableFuture.completedFuture(new ResponseObject("No face detected!", HttpStatus.BAD_REQUEST, null));
        }

        Mat inputFeatures = extractFaceFeatures(face);
        if (inputFeatures.empty()) {
            return CompletableFuture.completedFuture(new ResponseObject("Unable to extract face features!", HttpStatus.BAD_REQUEST, null));
        }

        boolean validSchedule = false;
        LocalDateTime now = LocalDateTime.now();

        for (User user : users) {
            Mat storedImage = convertByteArrayToMat(user.getAvata());
            if (storedImage.empty()) continue;

            Mat storedFace = detectAndNormalizeFace(storedImage);
            if (storedFace == null) continue;

            Mat storedFeatures = extractFaceFeatures(storedFace);
            if (storedFeatures.empty()) continue;

            double similarityScore = compareFeatures(inputFeatures, storedFeatures);
            if (similarityScore > 0.65) {
                Optional<Members> members = memberRepository.findByUser_Id(user.getId());
                if (!members.isPresent()) {
                    return CompletableFuture.completedFuture(new ResponseObject("Account not registered as a member", HttpStatus.OK, ""));
                }

                Members member = members.get();
                if (member.isExprire() || (member.getExpireDate() != null && member.getExpireDate().isBefore(now))) {
                    return CompletableFuture.completedFuture(new ResponseObject("Check-in failed! Membership has expired.", HttpStatus.BAD_REQUEST, null));
                }

                List<SchedulesIO> schedules = scheduleIORepository.findAllByMembers_Id(member.getId());

                for (SchedulesIO schedule : schedules) {
                    if (!schedule.getDate().toLocalDate().isEqual(now.toLocalDate())) {
                        continue;
                    }

                    LocalDateTime startTime = schedule.getDate().toLocalDate().atTime(6, 0);
                    LocalDateTime endTime = startTime.plusHours(schedule.getTime());

                    if (!now.isBefore(startTime) && now.isBefore(endTime)) {
                        schedule.setTimeCheckin(now);
                        schedule.setStatus(true);
                        scheduleIORepository.save(schedule);
                        validSchedule = true;
                        break;
                    } else {
                        String errorMessage = String.format("Check-in failed! Service: %s can only check in from %s to %s.",
                                member.getName(),
                                startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                                endTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                        return CompletableFuture.completedFuture(new ResponseObject(errorMessage, HttpStatus.BAD_REQUEST, null));
                    }
                }

                if (!validSchedule) {
                    return CompletableFuture.completedFuture(new ResponseObject("Not within training hours!", HttpStatus.BAD_REQUEST, null));
                }

                FaceReposi faceReposi = FaceReposi.builder()
                        .name(user.getId())
                        .goiTap(member.getName())
                        .hansd(1).build();

                return CompletableFuture.completedFuture(new ResponseObject("Check-in successful!", HttpStatus.OK, faceReposi));
            }
        }

        return CompletableFuture.completedFuture(new ResponseObject("No matching user found!", HttpStatus.BAD_REQUEST, null));
    }

    @Transactional
    public List<SchedulesIO> getSchedules(String memberId) {
        return scheduleIORepository.findAllByMembers_Id(memberId);
    }

    private Mat detectAndNormalizeFace(Mat image) {
        Mat grayImage = new Mat();
        opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.COLOR_BGR2GRAY);

        RectVector faces = new RectVector();
        faceDetector.detectMultiScale(grayImage, faces, 1.1, 3, 0, new Size(30, 30), new Size());

        if (faces.size() == 0) return null;

        Rect faceRect = faces.get(0);
        Mat face = new Mat(grayImage, faceRect);

        Mat normalizedFace = new Mat();
        opencv_imgproc.resize(face, normalizedFace, new Size(100, 100));
        opencv_imgproc.equalizeHist(normalizedFace, normalizedFace);

        return normalizedFace;
    }

    private Mat extractFaceFeatures(Mat face) {
        ORB orb = ORB.create();
        orb.setMaxFeatures(500);
        Mat descriptors = new Mat();
        KeyPointVector keyPoints = new KeyPointVector();
        orb.detectAndCompute(face, new Mat(), keyPoints, descriptors);
        return descriptors;
    }

    private double compareFeatures(Mat features1, Mat features2) {
        if (features1.empty() || features2.empty()) return 0.0;

        BFMatcher matcher = new BFMatcher(opencv_core.NORM_HAMMING, true);
        DMatchVector matches = new DMatchVector();
        matcher.match(features1, features2, matches);

        double totalDistance = 0.0;
        for (int i = 0; i < matches.size(); i++) {
            totalDistance += matches.get(i).distance();
        }

        double avgDistance = matches.size() > 0 ? totalDistance / matches.size() : Double.MAX_VALUE;
        return avgDistance < 100 ? 1.0 - (avgDistance / 100.0) : 0.0;
    }

    private Mat convertByteArrayToMat(byte[] imageBytes) {
        return opencv_imgcodecs.imdecode(new Mat(new BytePointer(imageBytes)), opencv_imgcodecs.IMREAD_COLOR);
    }

    private boolean isFaceExist(File imageFile) throws IOException {
        Mat image = opencv_imgcodecs.imread(imageFile.getAbsolutePath());
        if (image.empty()) {
            return false;
        }

        Mat grayImage = new Mat();
        opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.COLOR_BGR2GRAY);

        RectVector faces = new RectVector();
        faceDetector.detectMultiScale(grayImage, faces);

        int faceCount = (int) faces.size();
        image.release();
        grayImage.release();

        return faceCount == 1;
    }
}
