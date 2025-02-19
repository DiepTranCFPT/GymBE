package com.example.demo.config;// FirebaseProperties.java

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "firebase")
@RequiredArgsConstructor
public class FirebaseProperties {
    private final Config config;
    private final Storage storage;

    @Getter
    @RequiredArgsConstructor
    public static class Config {
        private final String projectId;
        private final Credentials credentials;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Credentials {
        private final String type;
        private final String projectId;
        private final String privateKeyId;
        private final String privateKey;
        private final String clientEmail;
        private final String clientId;
        private final String authUri;
        private final String tokenUri;
        private final String authProviderX509CertUrl;
        private final String clientX509CertUrl;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Storage {
        private final String bucket;
    }
}