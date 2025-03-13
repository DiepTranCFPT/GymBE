package com.gymsystem.cyber.model.Response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScheduleIORespone {

    private LocalDateTime dateTime;

    private LocalDateTime timeCheckin;


}
