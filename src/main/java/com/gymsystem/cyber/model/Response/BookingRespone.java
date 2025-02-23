package com.gymsystem.cyber.model.Response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Data
public class BookingRespone {
    private String email;
    private String member_name;
    private String trainer_name;
    private LocalDateTime date;
    private String plans;
}
