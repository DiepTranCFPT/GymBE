package com.gymsystem.cyber.model.Request;

import lombok.Data;

@Data
public class NotificationRequest {
    private String id;
    private String status;
    private String createAt;
}
