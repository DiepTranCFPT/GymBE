package com.gymsystem.cyber.controller;



import com.gymsystem.cyber.iService.iMember;
import com.gymsystem.cyber.model.Request.MemberRegistrationRequest;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.utils.AccountUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
public class BookingController {
   private final iMember memberService;


    public BookingController(iMember memberService, AccountUtils accountUtils) {
        this.memberService = memberService;

    }
    @PostMapping
    public ResponseObject registerMember(@RequestBody MemberRegistrationRequest member){
        return memberService.registerMember(member);
    }


}
