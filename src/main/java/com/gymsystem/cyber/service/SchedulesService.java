package com.gymsystem.cyber.service;

import com.gymsystem.cyber.iService.ISchedulesService;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.ScheduleIORepository;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class SchedulesService implements ISchedulesService {
     private final ScheduleIORepository scheduleIORepository;

    public SchedulesService(ScheduleIORepository scheduleIORepository) {
        this.scheduleIORepository = scheduleIORepository;
    }


    @Override
    @Transactional
    @Async
    public CompletableFuture<ResponseObject> countMembersByDay(LocalDateTime dateTime) {
        return CompletableFuture.supplyAsync(() -> {
            LocalDate startDate = dateTime.toLocalDate();
            LocalDateTime startOfDay = startDate.atStartOfDay();
            LocalDateTime endOfDay = startDate.atTime(23, 59, 59);
            int temp = scheduleIORepository.countMembersByDay(startOfDay, endOfDay);
            return ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Get all members on day!")
                    .data(temp)
                    .build();
        });
    }


    @Override
    @Transactional
    @Async
    public CompletableFuture<ResponseObject> countMembers(int month, int year) {
        return CompletableFuture.supplyAsync(() -> {
            int temp = scheduleIORepository.countTotalMembersByMonth(month, year);
            return ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Get all members on month!")
                    .data(temp)
                    .build();
        });
    }

}
