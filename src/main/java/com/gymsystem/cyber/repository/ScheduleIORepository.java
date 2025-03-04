package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Members;
import com.gymsystem.cyber.entity.SchedulesIO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleIORepository extends JpaRepository<SchedulesIO, String> {
        List<SchedulesIO> findByMembers(Members members);

}
