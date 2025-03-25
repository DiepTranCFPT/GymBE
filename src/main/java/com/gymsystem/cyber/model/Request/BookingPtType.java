package com.gymsystem.cyber.model.Request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BookingPtType(String emailUser, String emailPt, LocalDate date) {
}
