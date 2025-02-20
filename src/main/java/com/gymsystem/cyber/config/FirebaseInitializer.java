package com.gymsystem.cyber.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FirebaseInitializer {

//    @Value("${fcm.credentials.file.path}")
    private String firebaseCredentials = "firebase-admin.json";
    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        // Đảm bảo bạn đã tải file config từ Firebase
        FileInputStream serviceAccount = new FileInputStream(firebaseCredentials);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(com.google.auth.oauth2.GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        return FirebaseAuth.getInstance();
    }
}
