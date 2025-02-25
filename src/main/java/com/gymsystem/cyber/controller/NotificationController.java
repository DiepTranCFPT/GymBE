package com.gymsystem.cyber.controller;

import com.gymsystem.cyber.entity.Notifications;
import com.gymsystem.cyber.iService.INotify;
import com.gymsystem.cyber.model.Request.NotificationRequest;
import com.gymsystem.cyber.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final INotify notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/send")
    public String sendNotification(@RequestBody NotificationRequest notification) {
        notificationService.saveNotifacation(notification);
        messagingTemplate.convertAndSend("/topic/notifications/" + notification.getId(), notification);
        return "Notification sent success!";
    }
}
