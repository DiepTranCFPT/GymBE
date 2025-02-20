package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.*;
import com.gymsystem.cyber.iService.iMember;
import com.gymsystem.cyber.model.Request.MemberRegistrationRequest;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.MemberRepository;
import com.gymsystem.cyber.repository.MembershipPlansRepository;
import com.gymsystem.cyber.repository.PaymentRepository;
import com.gymsystem.cyber.utils.AccountUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MemberService implements iMember {
    private final MemberRepository memberRepository;
    private final AccountUtils accountUtils;
    private final MembershipPlansRepository membershipPlansRepository;
    private final PaymentRepository paymentRepository;


    public MemberService(MemberRepository memberRepository, AccountUtils accountUtils, MembershipPlansRepository membershipPlansRepository, PaymentRepository paymentRepository) {
        this.memberRepository = memberRepository;
        this.accountUtils = accountUtils;
        this.membershipPlansRepository = membershipPlansRepository;
        this.paymentRepository = paymentRepository;
    }


    @Override
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

            Subscriptions subscriptions = Subscriptions.builder()
                    .memberShipPlans(membershipPlansRepository.findByName(member.getName()).get())
                    .build();
            Payment transaction = Payment.builder()
                    .amount(member.getPrice())
                    .status(false)
                    .subscriptions(subscriptions)
                    .paymentMethod("VNPAY")
                    .build();
            Members members = Members.builder()
                    .name(member.getName())
                    .price(subscriptions.getMembers().getPrice())
                    .duration(member.getDuration())
                    .dateTime(LocalDateTime.now())
                    .subscriptions(subscriptions)
                    .user(user)
                    .exprire(false).build();

            memberRepository.save(members);
            paymentRepository.save(transaction);
            List<SchedulesIO> schedulesIO = members.getSchedulesIO();
            for (int i = 1 ; i <= members.getExpireDate().getDayOfMonth() - members.getDateTime().getDayOfMonth(); i++){
                schedulesIO.add(SchedulesIO.builder()
                        .members(members)
                        .date(members.getDateTime().plusDays(i))
                        .time(0)
                        .activity("Rest")
                        .status(false)
                        .build());
            }
            return ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("register successfully!")
                    .data(true)
                    .build();

    }
}
