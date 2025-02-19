//package com.example.demo.utils;
//
//import com.example.demo.entity.Account;
//import com.example.demo.service.EmailService;
//import com.example.model.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SendMailUtils {
//    @Autowired
//    EmailService emailService;
//
//    public void threadSendMail(Account user, String subject, String description) {
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                emailService.sendMail(user, subject, description);
//            }
//
//        };
//        new Thread(r).start();
//    }
//}
