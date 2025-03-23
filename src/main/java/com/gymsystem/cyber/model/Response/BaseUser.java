package com.gymsystem.cyber.model.Response;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record BaseUser(String id, LocalTime Checkin, LocalTime Checkout, String ptMail) {
}
