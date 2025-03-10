package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.Notifications;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.iService.INotify;
import com.gymsystem.cyber.model.Request.NotificationRequest;
import com.gymsystem.cyber.repository.AuthenticationRepository;
import com.gymsystem.cyber.repository.NotificationsRepository;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Optional;

@Service
public class NotificationService implements INotify {

    private final NotificationsRepository notificationsRepository;
    private final AuthenticationRepository  authenticationRepository;

    public NotificationService(NotificationsRepository notificationsRepository, AuthenticationRepository authenticationRepository) {
        this.notificationsRepository = notificationsRepository;
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public void saveNotifacation(NotificationRequest notificationRequest) throws AccountNotFoundException {

        User user = authenticationRepository.findById(notificationRequest.getId()).orElseThrow(()-> new AccountNotFoundException("Not found"));
        Notifications notifications = Notifications.builder()
                .user(user)
                .status(notificationRequest.getStatus())
                .createAt(notificationRequest.getCreateAt())
                .build();
        notificationsRepository.save(notifications);
    }
}
