package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.*;
import com.gymsystem.cyber.iService.iMember;
import com.gymsystem.cyber.model.Request.MemberRegistrationRequest;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.*;
import com.gymsystem.cyber.utils.AccountUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MemberService implements iMember {
    private final MemberRepository memberRepository;
    private final AccountUtils accountUtils;
    private final MembershipPlansRepository membershipPlansRepository;
    private final PaymentRepository paymentRepository;
    private final SubscriptonRepository subscriptionsRepository;
    private final ScheduleIORepository scheduleIORepository;


    public MemberService(MemberRepository memberRepository, AccountUtils accountUtils, MembershipPlansRepository membershipPlansRepository, PaymentRepository paymentRepository, SubscriptonRepository subscriptionsRepository, ScheduleIORepository scheduleIORepository) {
        this.memberRepository = memberRepository;
        this.accountUtils = accountUtils;
        this.membershipPlansRepository = membershipPlansRepository;
        this.paymentRepository = paymentRepository;
        this.subscriptionsRepository = subscriptionsRepository;
        this.scheduleIORepository = scheduleIORepository;
    }

    @Override
    @Transactional
    public ResponseObject registerMember(MemberRegistrationRequest member) {
        User user = accountUtils.getCurrentUser();
        if (user.getMembers() != null) {
            {
                return ResponseObject.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("You are already a member or Member is expire!")
                        .data(false)
                        .build();
            }
        }



        if(!membershipPlansRepository.existsByName(member.getName())){
                return ResponseObject.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("MemberShipPlan is not exist!")
                        .data(false)
                        .build();
        }

        Payment payment = Payment.builder()
                .id(UUID.randomUUID().toString())
                    .amount(membershipPlansRepository.findByName(member.getName()).getPrice())
                    .status(false)
                    .paymentMethod("VNPAY")
                    .build();
        paymentRepository.save(payment);
        Members members = Members.builder()
                .id(UUID.randomUUID().toString())
                    .name(member.getName())
                    .price(membershipPlansRepository.findByName(member.getName()).getPrice())
                    .duration(member.getDuration())
                    .dateTime(LocalDateTime.now())
                    .expireDate(LocalDateTime.now().plusMonths(member.getDuration()))
                    .user(user)
                    .exprire(false).build();
        memberRepository.save(members);

        Subscriptions subscriptions =  Subscriptions.builder()
                .id(UUID.randomUUID().toString())
                .memberShipPlans(membershipPlansRepository.findByName(member.getName()))
                .payment(payment)
                .members(members)
                .build();
        subscriptionsRepository.save(subscriptions);

        List<SchedulesIO> schedulesIO = new ArrayList<>();
        int daysBetween = (int) ChronoUnit.DAYS.between(members.getDateTime(), members.getExpireDate());
        for (int i = 1 ; i <= daysBetween; i++){
            schedulesIO.add(SchedulesIO.builder()
                    .id(UUID.randomUUID().toString())
                        .members(members)
                        .date(members.getDateTime().plusDays(i))
                        .time(1)
                        .activity("Rest")
                        .status(false)
                        .build());
            }
        try {
            scheduleIORepository.saveAll(schedulesIO);
        } catch (Exception e) {
            return ResponseObject.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Error saving data: " + e.getMessage())
                    .data(false)
                    .build();
        }

        return ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("register successfully!")
                    .data(true)
                    .build();
    }
}
