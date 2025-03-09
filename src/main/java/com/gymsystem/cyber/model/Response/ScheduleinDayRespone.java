package com.gymsystem.cyber.model.Response;

import com.google.firebase.database.annotations.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleinDayRespone {
    LocalDateTime checkin;
    LocalDateTime checkout;
    String name;
    String namePlans;
}
