package com.gymsystem.cyber.model.Response;

import com.google.type.DateTime;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SchedulesIORepo(String id, LocalDate dateTime, boolean checkin, boolean checkout,String UserEmail, String TrainerId) {
}
