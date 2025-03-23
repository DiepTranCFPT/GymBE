package com.gymsystem.cyber.model.Response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record CalendarTotal(LocalDate localDate, List<BaseUser> baseUsers) {
}
