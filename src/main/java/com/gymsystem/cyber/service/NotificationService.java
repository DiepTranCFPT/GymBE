package com.gymsystem.cyber.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.gymsystem.cyber.entity.Notifications;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.iService.INotify;
import com.gymsystem.cyber.model.Request.*;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.AuthenticationRepository;
import com.gymsystem.cyber.repository.NotificationsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class NotificationService implements INotify {

    private final NotificationsRepository notificationsRepository;
    private final AuthenticationRepository authenticationRepository;
    private final FirebaseMessaging firebaseMessaging;

    public NotificationService(NotificationsRepository notificationsRepository, AuthenticationRepository authenticationRepository, FirebaseMessaging firebaseMessaging) {
        this.notificationsRepository = notificationsRepository;
        this.authenticationRepository = authenticationRepository;
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    @Transactional
    public void saveNotifacation(NotificationRequest notificationRequest) {

        Notifications notifications = Notifications.builder()
                .user(notificationRequest.getUser())
                .status(notificationRequest.getStatus())
                .createAt(notificationRequest.getCreateAt())
                .dateTime(LocalDateTime.now())
                .build();
        notificationsRepository.save(notifications);
    }

    @Override
    public void sendNotification(String token, String Title, String content) {
//        try {
//            Message message = Message.builder()
//                    .setToken(token)
//                    .setNotification(Notification.builder()
//                            .setTitle(Title)
//                            .setBody(content)
//                            .build())
//                    .build();
//
//            FirebaseMessaging.getInstance().send(message);
//        } catch (FirebaseMessagingException e) {
//            throw new RuntimeException(e);
//        }
        try {
            Notification notification = Notification.builder()
                    .setTitle(Title)
                    .setBody(content)
                    .build();

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();

            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> getAllNotifications(String idUser) {
        List<RepoNotification> getAllNotifications = notificationsRepository.getAllByUser_Id(idUser)
                .stream()
                .map(notifications -> RepoNotification.builder()
                        .id(notifications.getId())
                        .header(notifications.getStatus())
                        .content(notifications.getCreateAt())
                        .dateTime(notifications.getDateTime())
                        .build())
                .toList();

        return CompletableFuture.completedFuture(ResponseObject.builder()
                .data(getAllNotifications)
                .httpStatus(HttpStatus.OK)
                .message("Danh sách thông báo!")
                .build());
    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> createNotification(TypeNotification typeNotifitation,
                                                                BookingPtType bkpt,
                                                                BookingServiceType bookingServiceType,
                                                                Checkinout checkinout, String content) {
        ResponseObject responseObject = ResponseObject.builder()
                .message(typeNotifitation.toString())
                .data(true)
                .httpStatus(HttpStatus.OK)
                .build();


        switch (typeNotifitation) {
            case AMIN -> {
                List<User> users = authenticationRepository.findAll()
                        .stream().filter(user -> !user.isDeleted())
                        .collect(Collectors.toList());
                List<Notifications> notifications = new ArrayList<>();
                users.forEach(user -> {
                    notifications.add(Notifications.builder()
                            .createAt(content)
                            .user(user)
                            .status(TypeNotification.AMIN.name())
                            .dateTime(LocalDateTime.now())
                            .build());
                    if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
                        sendNotification(user.getFcmToken(), TypeNotification.AMIN.name(), content);
                    }
                });
                notificationsRepository.saveAllAndFlush(notifications);

                responseObject.setData(content);
                return CompletableFuture.completedFuture(responseObject);
            }
            case CHECKIN -> { //
                String contents = "";
                if (checkinout.checkin() != null && checkinout.checkout() == null)
                    contents = "Checkin: " + checkinout.checkin().toLocalDate() + " " + checkinout.checkin().toLocalTime();

                User admin = authenticationRepository.findAllByEmail("swpproject2024@gmail.com");
                User user = authenticationRepository.findById(checkinout.userId()).orElseThrow(() -> new RuntimeException("User not found: "));

                saveNotifacation(NotificationRequest.builder()
                        .user(user)
                        .status(TypeNotification.CHECKIN.name())
                        .createAt(content)
                        .build());

                String contentAdmin = "Checkin:(" + user.getEmail() + ") " + checkinout.checkin().toLocalDate() + " " + checkinout.checkin().toLocalTime();
                saveNotifacation(NotificationRequest.builder()
                        .user(admin)
                        .status(TypeNotification.CHECKIN.name())
                        .createAt(contentAdmin)
                        .build());
                if (user.getFcmToken() != null)
                    sendNotification(user.getFcmToken(), TypeNotification.CHECKIN.name(), contents);
                if (admin.getFcmToken() != null)
                    sendNotification(admin.getFcmToken(), TypeNotification.CHECKIN.name(), contentAdmin);
                responseObject.setData(contents);
                return CompletableFuture.completedFuture(responseObject);
            }
            case CHECHOUT -> { //
                String contents = "";
                if (checkinout.checkin() == null && checkinout.checkout() != null)
                    contents = "Checkout: " + checkinout.checkout().toLocalDate() + " " + checkinout.checkout().toLocalTime();

                User admin = authenticationRepository.findAllByEmail("swpproject2024@gmail.com");
                User user = authenticationRepository.findById(checkinout.userId()).orElseThrow(() -> new RuntimeException("User not found: "));

                saveNotifacation(NotificationRequest.builder()
                        .user(user)
                        .status(TypeNotification.CHECHOUT.name())
                        .createAt(content)
                        .build());

                String contentAdmin = "Checkin:(" + user.getEmail() + ") " + checkinout.checkout().toLocalDate() + " " + checkinout.checkout().toLocalTime();
                saveNotifacation(NotificationRequest.builder()
                        .user(admin)
                        .status(TypeNotification.CHECKIN.name())
                        .createAt(contentAdmin)
                        .build());
                if (user.getFcmToken() != null)
                    sendNotification(user.getFcmToken(), TypeNotification.CHECHOUT.name(), contents);
                if (admin.getFcmToken() != null)
                    sendNotification(admin.getFcmToken(), TypeNotification.CHECHOUT.name(), contentAdmin);
                responseObject.setData(contents);
                return CompletableFuture.completedFuture(responseObject);
            }
            case BOOKING_SERVICE -> { //

                String contents = "Đặt thành công gòi dịch vụ:\n" + bookingServiceType.serviceBooking().name() + "\n" + "Số lượng :" + bookingServiceType.serviceBooking().sl() +
                        "Tổng tiền: " + bookingServiceType.serviceBooking().total() + "\n" + "Chi tiết: " + bookingServiceType.dt();

                User user = authenticationRepository.findById(bookingServiceType.userId())
                        .orElseThrow(() -> new RuntimeException("User not found: " + bookingServiceType.userId()));

                User admin = authenticationRepository.findAllByEmail("swpproject2024@gmail.com");

                saveNotifacation(NotificationRequest.builder()
                        .user(user)
                        .status(TypeNotification.BOOKING_SERVICE.name())
                        .createAt(contents)
                        .build());
                saveNotifacation(NotificationRequest.builder()
                        .user(admin)
                        .status(TypeNotification.BOOKING_SERVICE.name())
                        .createAt(contents)
                        .build());
                if (user.getFcmToken() != null)
                    sendNotification(user.getFcmToken(), TypeNotification.BOOKING_SERVICE.name(), contents);
                if (admin.getFcmToken() != null)
                    sendNotification(admin.getFcmToken(), TypeNotification.BOOKING_SERVICE.name(), contents);
                responseObject.setData(contents);
                return CompletableFuture.completedFuture(responseObject);
            }
            case BOOKING_PT -> { //
                String contenUser = "Bạn đã gửi yêu cầu đến PT " + bkpt.emailPt() + " vào ngày: " + bkpt.date();
                String contenPt = "Bạn đã nhận được yêu cầu luyện tập của học viên " + bkpt.emailUser() + " vào ngày: " + bkpt.date();

                User user = authenticationRepository.findAllByEmail(bkpt.emailUser());
                User pt = authenticationRepository.findAllByEmail(bkpt.emailPt());

                saveNotifacation(NotificationRequest.builder()
                        .user(user)
                        .status(TypeNotification.BOOKING_PT.name())
                        .createAt(contenUser)
                        .build());
                saveNotifacation(NotificationRequest.builder()
                        .user(pt)
                        .status(TypeNotification.BOOKING_PT.name())
                        .createAt(contenPt)
                        .build());
                if (user.getFcmToken() != null)
                    sendNotification(user.getFcmToken(), TypeNotification.BOOKING_PT.name(), contenUser);
                if (pt.getFcmToken() != null)
                    sendNotification(pt.getFcmToken(), TypeNotification.BOOKING_PT.name(), contenPt);

                responseObject.setData(contenUser + " , " + contenPt);
                return CompletableFuture.completedFuture(responseObject);
            }
            default -> { ///
                responseObject.setData("Lỗi không mong muốn.");
                return CompletableFuture.completedFuture(responseObject);
            }
        }
    }
}
