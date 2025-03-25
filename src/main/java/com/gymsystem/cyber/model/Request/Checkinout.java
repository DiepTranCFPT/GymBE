package com.gymsystem.cyber.model.Request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Checkinout(String userId, LocalDateTime checkin, LocalDateTime checkout) {
}
