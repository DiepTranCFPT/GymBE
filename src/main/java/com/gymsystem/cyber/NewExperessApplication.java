package com.gymsystem.cyber;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.IOException;


@SpringBootApplication(scanBasePackages = "com.gymsystem.cyber")
@EntityScan(basePackages = {"com.gymsystem.cyber.entity"})
public class NewExperessApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewExperessApplication.class, args);
        openSwaggerUI();
    }

    private static void openSwaggerUI() {
        String url = "http://localhost:8080/swagger-ui/index.html";
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win"))
                new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", url).start();
        } catch (IOException ignored) {
            throw new RuntimeException();
        }
    }

}
