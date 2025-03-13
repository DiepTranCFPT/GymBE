package com.gymsystem.cyber.iService;

import com.gymsystem.cyber.entity.Notifications;
import com.gymsystem.cyber.model.Request.NotificationRequest;

import javax.security.auth.login.AccountNotFoundException;

public interface INotify {
    void saveNotifacation(NotificationRequest notifications) throws AccountNotFoundException;
}