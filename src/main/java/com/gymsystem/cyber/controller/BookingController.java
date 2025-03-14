package com.gymsystem.cyber.controller;


import com.gymsystem.cyber.iService.iMember;
import com.gymsystem.cyber.model.Request.MemberRegistrationRequest;
import com.gymsystem.cyber.model.Request.PTforUserRequest;
import com.gymsystem.cyber.model.Request.PTscheduleRequest;
import com.gymsystem.cyber.model.ResponseObject;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/booking")
public class BookingController {

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    private final iMember memberService;

    public BookingController(iMember memberService) {
        this.memberService = memberService;
    }


    @PostMapping
    @Operation(summary = "tao 1 booking ")
    public ResponseObject registerMember(@RequestBody MemberRegistrationRequest member) {
        ResponseObject responseObject = memberService.registerMember(member);

        if (responseObject.getMessage().equals("register successfully!")) {
            messagingTemplate.convertAndSend("/topic/notifications/", "Đăng ký thành công!");
        } else {
            messagingTemplate.convertAndSend("/topic/notifications", "Đăng ký thất bại!");
        }
        return responseObject;
    }

    @GetMapping
    public ResponseObject getMember() {
        return memberService.getAllMembers();
    }

    @PostMapping("/trainnerforuser")
    public ResponseObject trainerforUser(@RequestBody PTforUserRequest pTforUserRequest) throws AccountNotFoundException {
        return memberService.regisPTForUser(pTforUserRequest);
    }

    @PostMapping("/regis-pt")
    public ResponseObject regisPTBySchedule(@RequestParam PTscheduleRequest pTscheduleRequest) throws AccountNotFoundException {
        return memberService.regisPTForSchedule(pTscheduleRequest);
    }

    @GetMapping("/detail-userlist/{id}")
    @Operation(summary = "lay tat ca cac dich vu ma user da su dung tren he thong")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CompletableFuture<ResponseObject> responseObjectCompletableFuture(@PathVariable("id") String id) {
        return memberService.getMemberByUserId(id);
    }

}
