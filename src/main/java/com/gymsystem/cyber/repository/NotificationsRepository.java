package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, String> {

    List<Notifications> getAllByUser_Id(String userId);

}
