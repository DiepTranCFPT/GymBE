package com.gymsystem.cyber.model.Request;

import com.google.firebase.database.annotations.NotNull;
import lombok.Data;

@Data
public class NotificationRequest {
    @NotNull( "ID must not be null")
    private String id;

    @NotNull("Status must not be null")
    private String status;

    @NotNull("Creation date must not be null")
    private String createAt;

    // Getters and setters
}

