package com.gymsystem.cyber.model.Request;

import com.google.type.DateTime;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record ServiceBooking(String name, double total,int sl, LocalTime dateTime) {
}
