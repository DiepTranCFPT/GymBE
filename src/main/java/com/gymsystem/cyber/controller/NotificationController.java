package com.gymsystem.cyber.controller;

import com.gymsystem.cyber.entity.Notifications;
import com.gymsystem.cyber.iService.INotify;
import com.gymsystem.cyber.model.Request.NotificationRequest;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.concurrent.CompletableFuture;

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
    public String sendNotification(@RequestBody NotificationRequest notification) throws AccountNotFoundException {
//        notificationService.saveNotifacation(notification);
//        messagingTemplate.convertAndSend("/topic/notifications/" + notification.getId(), notification);
        return "Notification sent success!";
    }

    @GetMapping("/{id}")
    @Operation(summary = "lay tat ca ca thong bao cua nguoi dung voi id")
    public CompletableFuture<ResponseObject> getNotifications(@PathVariable("id") String id) {
        return notificationService.getAllNotifications(id);
    }
}
