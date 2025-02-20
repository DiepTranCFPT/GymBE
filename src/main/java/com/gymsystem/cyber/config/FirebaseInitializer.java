package com.gymsystem.cyber.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@PropertySource("classpath:application.properties")
public class FirebaseInitializer {

    private final String firebaseCredentials = "./FirebaseSetting.json";

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        InputStream serviceAccount = new ClassPathResource(firebaseCredentials).getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(com.google.auth.oauth2.GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        return FirebaseAuth.getInstance();
    }
}
