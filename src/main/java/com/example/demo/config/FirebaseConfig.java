package com.example.demo.config;// FirebaseConfig.java

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {
    private final FirebaseProperties firebaseProperties;

    public FirebaseConfig(FirebaseProperties firebaseProperties) {
        this.firebaseProperties = firebaseProperties;
    }

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // Delete any existing apps
        FirebaseApp.getApps().forEach(FirebaseApp::delete);

        // Construct JSON structure for credentials
        String credentialsJson = String.format(
                "{\n" +
                        "  \"type\": \"%s\",\n" +
                        "  \"project_id\": \"%s\",\n" +
                        "  \"private_key_id\": \"%s\",\n" +
                        "  \"private_key\": \"%s\",\n" +
                        "  \"client_email\": \"%s\",\n" +
                        "  \"client_id\": \"%s\",\n" +
                        "  \"auth_uri\": \"%s\",\n" +
                        "  \"token_uri\": \"%s\",\n" +
                        "  \"auth_provider_x509_cert_url\": \"%s\",\n" +
                        "  \"client_x509_cert_url\": \"%s\"\n" +
                        "}",
                firebaseProperties.getConfig().getCredentials().getType(),
                firebaseProperties.getConfig().getCredentials().getProjectId(),
                firebaseProperties.getConfig().getCredentials().getPrivateKeyId(),
                firebaseProperties.getConfig().getCredentials().getPrivateKey(),
                firebaseProperties.getConfig().getCredentials().getClientEmail(),
                firebaseProperties.getConfig().getCredentials().getClientId(),
                firebaseProperties.getConfig().getCredentials().getAuthUri(),
                firebaseProperties.getConfig().getCredentials().getTokenUri(),
                firebaseProperties.getConfig().getCredentials().getAuthProviderX509CertUrl(),
                firebaseProperties.getConfig().getCredentials().getClientX509CertUrl()
        );

        ByteArrayInputStream credentialsStream = new ByteArrayInputStream(
                credentialsJson.getBytes(StandardCharsets.UTF_8));

        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId(firebaseProperties.getConfig().getProjectId())
                .setStorageBucket(firebaseProperties.getStorage().getBucket())
                .build();

        // Initialize as default app
        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public StorageClient storageClient(FirebaseApp firebaseApp) {
        return StorageClient.getInstance(firebaseApp);
    }
}