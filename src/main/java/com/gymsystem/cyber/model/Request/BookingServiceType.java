package com.gymsystem.cyber.model.Request;

import lombok.Builder;

@Builder
public record BookingServiceType(ServiceBooking serviceBooking,String userId, String dt) {
}
