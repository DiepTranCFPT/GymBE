package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.SchedulesIO;
import com.gymsystem.cyber.model.Response.BookingRespone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleIORepository extends JpaRepository<SchedulesIO, String> {


}
