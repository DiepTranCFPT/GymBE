package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Members;
import com.gymsystem.cyber.entity.SchedulesIO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleIORepository extends JpaRepository<SchedulesIO, String> {
        @Query("SELECT s FROM SchedulesIO s WHERE s.members.id = :membersId")
        List<SchedulesIO> findAllByMembers_Id(@Param("membersId") String membersId);


        Optional<SchedulesIO> findByMembers_IdAndStatusTrueAndDateBetween(String memberId, LocalDateTime startOfDay, LocalDateTime endOfDay);

        List<SchedulesIO> findByMembers(Members members);

        Optional<SchedulesIO> findById(String id);


        boolean existsByMembers_Id(String membersId);

        int countByMembers_Id(String membersId);


}
