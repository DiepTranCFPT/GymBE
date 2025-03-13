package com.gymsystem.cyber.model.Response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record GetUserCategory(String id, java.time.LocalTime checkin, LocalTime checkout, String idPt) {
}
