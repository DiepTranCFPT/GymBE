package com.gymsystem.cyber.model.Request;

import com.google.firebase.database.annotations.NotNull;
import com.gymsystem.cyber.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequest {
//    @NotNull( "ID must not be null")
//    private String id;
    private User user;

    @NotNull("Status must not be null")
    private String status;

    @NotNull("Creation date must not be null")
    private String createAt;

}

