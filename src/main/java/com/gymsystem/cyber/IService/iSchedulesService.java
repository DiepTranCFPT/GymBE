package com.gymsystem.cyber.iService;
import java.time.LocalDateTime;


public interface ISchedulesService {
    int countMembersByDay(LocalDateTime date);
    int countMembers(int month, int year);
}
