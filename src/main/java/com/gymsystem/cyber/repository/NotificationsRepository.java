package com.gymsystem.cyber.repository;

import com.gymsystem.cyber.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationsRepository extends JpaRepository<Notifications, String> {
}
