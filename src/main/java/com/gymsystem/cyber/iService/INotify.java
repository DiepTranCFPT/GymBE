package com.gymsystem.cyber.iService;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.gymsystem.cyber.model.Request.*;
import com.gymsystem.cyber.model.ResponseObject;

import javax.security.auth.login.AccountNotFoundException;
import java.util.concurrent.CompletableFuture;

public interface INotify {
    void saveNotifacation(NotificationRequest notifications) throws AccountNotFoundException;

    void sendNotification(String iduse, String Title, String content) throws FirebaseMessagingException;

    CompletableFuture<ResponseObject> getAllNotifications(String idUser);

    CompletableFuture<ResponseObject> createNotification(TypeNotification typeNotifitation,
                                                         BookingPtType bkpt,
                                                         BookingServiceType bookingServiceType,
                                                         Checkinout checkinout, String content);
}