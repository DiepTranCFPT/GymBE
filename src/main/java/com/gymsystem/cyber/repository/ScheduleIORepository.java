package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Members;
import com.gymsystem.cyber.entity.SchedulesIO;
import com.gymsystem.cyber.model.Response.BookingRespone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleIORepository extends JpaRepository<SchedulesIO, String> {
        List<SchedulesIO> findByMembers(Members members);
}
