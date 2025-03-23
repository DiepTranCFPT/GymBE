package com.gymsystem.cyber.model.Response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DetailBooking(String nameService, LocalDate start,
                            LocalDate end,
                            double price,
                            String usermail) {
}
