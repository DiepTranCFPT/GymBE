package com.gymsystem.cyber.service;

import com.gymsystem.cyber.iService.ISchedulesService;
import com.gymsystem.cyber.repository.ScheduleIORepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class SchedulesService implements ISchedulesService {
     private final ScheduleIORepository scheduleIORepository;

    public SchedulesService(ScheduleIORepository scheduleIORepository) {
        this.scheduleIORepository = scheduleIORepository;
    }


    @Override
    public int countMembersByDay(LocalDateTime dateTime) {
        LocalDate startDate = dateTime.toLocalDate();
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = startDate.atTime(23, 59, 59);
        return scheduleIORepository.countMembersByDay(startOfDay, endOfDay);
    }


    @Override
    public int countMembers(int month, int year) {
        return scheduleIORepository.countTotalMembersByMonth(month, year);
    }
}
