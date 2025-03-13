package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.*;
import com.gymsystem.cyber.iService.iMember;
import com.gymsystem.cyber.model.Request.MemberRegistrationRequest;
import com.gymsystem.cyber.model.Request.PTforUserRequest;
import com.gymsystem.cyber.model.Request.PTscheduleRequest;
import com.gymsystem.cyber.model.Response.BookingRespone;
import com.gymsystem.cyber.model.Response.DetaileUserinSystem;
import com.gymsystem.cyber.model.Response.MemberPlans;
import com.gymsystem.cyber.model.Response.MembersRC;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.*;
import com.gymsystem.cyber.utils.AccountUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class MemberService implements iMember {
    private final MemberRepository memberRepository;
    private final AccountUtils accountUtils;
    private final MembershipPlansRepository membershipPlansRepository;
    private final PaymentRepository paymentRepository;
    private final SubscriptonRepository subscriptionsRepository;
    private final ScheduleIORepository scheduleIORepository;
    private final AuthenticationRepository authenticationRepository;
    private final TrainerRepository trainerRepository;


    public MemberService(MemberRepository memberRepository, AccountUtils accountUtils, MembershipPlansRepository membershipPlansRepository, PaymentRepository paymentRepository, SubscriptonRepository subscriptionsRepository, ScheduleIORepository scheduleIORepository, AuthenticationRepository authenticationRepository, TrainerRepository trainerRepository) {
        this.memberRepository = memberRepository;
        this.accountUtils = accountUtils;
        this.membershipPlansRepository = membershipPlansRepository;
        this.paymentRepository = paymentRepository;
        this.subscriptionsRepository = subscriptionsRepository;
        this.scheduleIORepository = scheduleIORepository;
        this.authenticationRepository = authenticationRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    @Transactional
    public ResponseObject registerMember(MemberRegistrationRequest member) {
        User user = accountUtils.getCurrentUser();
        MemberShipPlans memberShipPlans = membershipPlansRepository.findById(member.getId()).orElse(null);
        if(user.getMembers() !=null){
            if (user.getMembers().getExpireDate().isBefore(LocalDateTime.now())) {
                {
                    return ResponseObject.builder()
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .message("You are already a member or Member is expire!")
                            .data(false)
                            .build();
                }
            }
        }

        if (!memberShipPlans.isActive()) {
            return ResponseObject.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("MemberShipPlan is not exist or is expired!")
                    .data(false)
                    .build();
        }

        Payment payment = Payment.builder()
                .amount(memberShipPlans.getPrice())
                .status(false)
                .paymentMethod("VNPAY")
                .build();

        Members members = Members.builder()
                .name(user.getEmail() + ":" + memberShipPlans.getName() + ":" + LocalDate.now())
                .price(memberShipPlans.getPrice() * member.getDuration())
                .duration(member.getDuration())
                .dateTime(LocalDateTime.now())
                .expireDate(LocalDate.now().atTime(6 + memberShipPlans.getTimeInDay(),0).plusMonths(member.getDuration()))
                .user(user)
                .exprire(false).build();

        Subscriptions subscriptions = Subscriptions.builder()
                .memberShipPlans(memberShipPlans)
                .payment(payment)
                .members(members)
                .build();


        List<SchedulesIO> schedulesIO = new ArrayList<>();
        int daysBetween = (int) ChronoUnit.DAYS.between(members.getDateTime(), members.getExpireDate());
        for (int i = 0; i <= daysBetween; i++) {
            schedulesIO.add(SchedulesIO.builder()
                    .id(UUID.randomUUID().toString())
                    .members(members)
                    .date(members.getDateTime().plusDays(i))
                    .activity("Rest")
                    .status(false)
                    .build());
        }
        paymentRepository.save(payment);
        subscriptionsRepository.save(subscriptions);
        memberRepository.save(members);
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

    @Override
    public ResponseObject getAllMembers() {
        List<SchedulesIO> schedulesIOS = scheduleIORepository.findAll();
        List<BookingRespone> bookingRespones = new ArrayList<>();
        for (SchedulesIO schedulesIO : schedulesIOS) {
            if (schedulesIO.getDate().getDayOfYear() == LocalDateTime.now().getDayOfYear() + 1 && schedulesIO.getDate().getYear() == LocalDateTime.now().getYear()) {
                bookingRespones.add(BookingRespone.builder()
                        .email(schedulesIO.getMembers().getUser().getEmail())
                        .member_name(schedulesIO.getMembers().getUser().getName())
                        .trainer_name("Trainer")
                        .date(schedulesIO.getDate())
                        .plans(schedulesIO.getMembers().getSubscriptions().getMemberShipPlans().getName())
                        .build());
            }
        }
        return ResponseObject.builder()
                .httpStatus(HttpStatus.OK)
                .message("Get all members successfully!")
                .data(bookingRespones)
                .build();
    }

    @Override
    @Transactional
    public ResponseObject regisPTForUser(PTforUserRequest pTforUserRequest) throws AccountNotFoundException {
        SchedulesIO schedulesIO = scheduleIORepository.findById(pTforUserRequest.getIdScheduleIo()).orElse(null);


        if (schedulesIO == null && schedulesIO.getDate().toLocalDate().isBefore(LocalDate.now())) {
            throw new AccountNotFoundException();
        }
        if (schedulesIO.getTrainer() != null) {
            throw new AccountNotFoundException("Trainer is already registered");
        }

        Trainer trainer = trainerRepository.findById(pTforUserRequest.getIdTrainer())
                .orElseThrow(() -> new AccountNotFoundException("Trainer Not Found"));

        schedulesIO.setTrainer(trainer);

        scheduleIORepository.saveAndFlush(schedulesIO);

        return ResponseObject.builder()
                .httpStatus(HttpStatus.OK)
                .message("RegisPT " + trainer.getUser().getName() + " Success")
                .build();
    }

    @Override
    public ResponseObject regisPTForSchedule(PTscheduleRequest pTscheduleRequest) throws AccountNotFoundException {
        User user = accountUtils.getCurrentUser();

        SchedulesIO schedulesIO = scheduleIORepository.findById(pTscheduleRequest.getIdSchedule()).orElseThrow(() -> new AccountNotFoundException("Schedule does not exist in account"));


        Trainer trainer = trainerRepository.findById(pTscheduleRequest.getIdPT()).orElseThrow(() -> new AccountNotFoundException("Trainer Not Found"));

        if (!trainer.isStatus()) {
            schedulesIO.setTrainer(trainer);
            trainer.setStatus(true);
            scheduleIORepository.save(schedulesIO);
            return ResponseObject.builder().message("Success").build();
        } else {
            return ResponseObject.builder().message("fail").build();
        }
    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> getMemberByUserId(String id) {



        List<Members> members = memberRepository.findAllByUser_Id(id);

        List<DetaileUserinSystem> detaileUserinSystems = new ArrayList<>();

        if (members != null && members.size() > 0) {
            for (Members member : members) {
                Subscriptions subscriptions = subscriptionsRepository.findByMembers_Id(member.getId()).orElse(null);
                if (subscriptions != null) {
                    MemberShipPlans memberShipPlans = membershipPlansRepository.findById(subscriptions.getMemberShipPlans().getId()).orElse(null);
                    if (memberShipPlans != null) {

                        detaileUserinSystems.add(DetaileUserinSystem.builder()
                                .id(member.getId())
                                .memberPlans(MemberPlans.builder()
                                        .id(memberShipPlans.getId())
                                        .datestart(memberShipPlans.getStartDate().toLocalDate())
                                        .enddate(memberShipPlans.getEndDate().toLocalDate())
                                        .timeinday(memberShipPlans.getTimeInDay())
                                        .name(memberShipPlans.getName())
                                        .price(memberShipPlans.getPrice())
                                        .build())
                                .members(MembersRC.builder()
                                        .datestart(member.getDateTime().toLocalDate())
                                        .enddate(member.getExpireDate().toLocalDate())
                                        .id(member.getId())
                                        .price(member.getPrice())
                                        .duration(member.getDuration())
                                        .build())
                                .build());
                    }
                }
            }
        }
        return CompletableFuture.completedFuture(ResponseObject.builder()
                .httpStatus(HttpStatus.OK)
                .message("Get members successfully!")
                .data(detaileUserinSystems)
                .build());
    }
}
