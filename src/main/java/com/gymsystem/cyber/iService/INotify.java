package com.gymsystem.cyber.iService;

import com.gymsystem.cyber.entity.Notifications;
import com.gymsystem.cyber.model.Request.NotificationRequest;

public interface INotify {
    void saveNotifacation(NotificationRequest notifications);
}