package com.gymsystem.cyber.service;

import com.gymsystem.cyber.iService.IFaceRecodeService;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.AuthenticationRepository;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

@Service
public class FaceRecodeService implements IFaceRecodeService {

    private final AuthenticationRepository authenticationRepository;

    public FaceRecodeService(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> regisFaceIDforAccount(String email, MultipartFile file) throws IOException {
        ResponseObject responseObject;

        User user = authenticationRepository.findByEmail(email).map(user1 -> {
            if (user1.isDeleted()) {
                throw new UsernameNotFoundException("Account has been deleted!");
            }
            if (!user1.isEnable()) {
                throw new UsernameNotFoundException("Account is not enabled!");
            }
            return user1;
        }).orElseThrow(() -> new UsernameNotFoundException("Account not exist!"));
        File tempfile = File.createTempFile("face_", ".jpg");
        file.transferTo(tempfile);

        try {
            if (!isFaceExist(tempfile)) {
                return CompletableFuture.completedFuture(ResponseObject.builder()
                        .message("Face not found!")
                        .data(false)
                        .httpStatus(HttpStatus.OK)
                        .build());
            }
            byte[] imageBytes = Files.readAllBytes(tempfile.toPath());
            user.setAvata(imageBytes);
            authenticationRepository.saveAndFlush(user);
            responseObject = ResponseObject.builder()
                    .message("Faceid successfully")
                    .data(false)
                    .httpStatus(HttpStatus.OK)
                    .build();
        } finally {
            tempfile.delete();
        }
        return CompletableFuture.completedFuture(responseObject);
    }

    private boolean isFaceExist(File imageFile) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("haarcascade_frontalface_default.xml");
        if (inputStream == null) {
            throw new FileNotFoundException("Cascade file not found!");
        }

        // Tạo file tạm để OpenCV đọc
        File tempFile = File.createTempFile("cascade_", ".xml");
        Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        CascadeClassifier faceDetector = new CascadeClassifier(tempFile.getAbsolutePath());

        Mat image = Imgcodecs.imread(imageFile.getAbsolutePath());
        if (image.empty()) {
            return false;
        }

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        int faceCount = faceDetections.toArray().length;
        image.release();

        return faceCount == 1;
    }

}
