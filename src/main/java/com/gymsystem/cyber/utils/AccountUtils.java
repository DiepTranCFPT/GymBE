package com.gymsystem.cyber.utils;


import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.repository.AuthenticationRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class AccountUtils {


    private final AuthenticationRepository userRepository;

    public AccountUtils(AuthenticationRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * random 1 ma code voi 6 so tu 000001 - 999999
     *
     * @return {@link String} code random
     */
    public static String generateRandomNumberString() {
        return String.format("%06d", new Random().nextInt(100000));
    }

    /**
     * ktr email
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        String regex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        return Pattern.matches(regex, email);
    }

    /**
     * ktr sdt
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Regex kiểm tra số điện thoại (10-11 chữ số)
        String regex = "^[0-9]{10,11}$";
        return Pattern.matches(regex, phoneNumber);
    }

    public User getCurrentUser(){
        String email =  SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    public static byte[] matToByteArray(Mat mat) throws IOException {
        Path tempFile = Files.createTempFile("face", ".jpg");
        Imgcodecs.imwrite(tempFile.toString(), mat);
        byte[] imageBytes = Files.readAllBytes(tempFile);
        Files.delete(tempFile);
        return imageBytes;

    }
}
