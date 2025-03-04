package com.gymsystem.cyber.controller;



import com.gymsystem.cyber.iService.iMember;
import com.gymsystem.cyber.model.Request.MemberRegistrationRequest;
import com.gymsystem.cyber.model.Request.PTforUserRequest;
import com.gymsystem.cyber.model.ResponseObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired SimpMessagingTemplate messagingTemplate;

   private final iMember memberService;
   public BookingController(iMember memberService) {
        this.memberService = memberService;
    }
    @PostMapping
    public ResponseObject registerMember(@RequestBody MemberRegistrationRequest member){
        ResponseObject responseObject = memberService.registerMember(member);

        if(responseObject.getMessage().equals("register successfully!")){
            messagingTemplate.convertAndSend("/topic/notifications/" ,"Đăng ký thành công!");
        }else {
            messagingTemplate.convertAndSend("/topic/notifications", "Đăng ký thất bại!");
        }
        return responseObject;
    }
    @GetMapping
    public ResponseObject getMember(){
        return memberService.getAllMembers();
    }

    @PostMapping("/Trainnerforuser")
    public ResponseObject trainerforUser(@RequestBody  PTforUserRequest pTforUserRequest) throws AccountNotFoundException {
       return memberService.regisPTForUser(pTforUserRequest);
    }
}
