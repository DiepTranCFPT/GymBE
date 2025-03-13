package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.MemberShipPlans;
import com.gymsystem.cyber.entity.SchedulesIO;
import com.gymsystem.cyber.iService.IMemberShipPlans;
import com.gymsystem.cyber.model.Request.PlansRequest;
import com.gymsystem.cyber.model.Response.GetUserCategory;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.MembershipPlansRepository;
import com.gymsystem.cyber.repository.ScheduleIORepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MembershipPlanService implements IMemberShipPlans {

    private final MembershipPlansRepository membershipPlansRepository;
    private final ScheduleIORepository scheduleIORepository;

    public MembershipPlanService(MembershipPlansRepository membershipPlansRepository, ScheduleIORepository scheduleIORepository) {
        this.membershipPlansRepository = membershipPlansRepository;
        this.scheduleIORepository = scheduleIORepository;
    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> getMembershipPlans() {
        List<MemberShipPlans> memberShipPlans = membershipPlansRepository.findAll();

        List<PlansRequest> plansRequests = new ArrayList<>();

        for (MemberShipPlans check : memberShipPlans) {
            if (!check.isActive()) {
                continue;
            }
            PlansRequest plansRequest = new PlansRequest();
            plansRequest.setId(check.getId()); /// gui id ve client
            plansRequest.setName(check.getName());
            plansRequest.setDescription(check.getDescription());
            plansRequest.setPrice(check.getPrice());
            plansRequest.setEndDate(check.getEndDate().toLocalDate());
            plansRequest.setStartedDate(check.getStartDate().toLocalDate());
            plansRequest.setTimeInDay(check.getTimeInDay()); /// lay tg hoat dong toi da trong 1 ngay cua goi.

            plansRequests.add(plansRequest);
        }

        return CompletableFuture.completedFuture(new ResponseObject("Success", HttpStatus.OK, plansRequests));
    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> getMembershipPlan(String id) { ///  get = id
//        MemberShipPlans memberShipPlans = membershipPlansRepository.findByName(name);
        MemberShipPlans memberShipPlans = membershipPlansRepository.findById(id).orElse(null);

        if (memberShipPlans == null && !memberShipPlans.isActive()) {
            return CompletableFuture.completedFuture(new ResponseObject("Fail", HttpStatus.BAD_REQUEST, "Membership Plan is not exist"));
        }
        // DIEP K map ve DTO no se sap db do :((
        return CompletableFuture.completedFuture(new ResponseObject("Success", HttpStatus.OK,
                PlansRequest.builder()
                        .id(memberShipPlans.getId())
                        .startedDate(memberShipPlans.getEndDate().toLocalDate())
                        .description(memberShipPlans.getDescription())
                        .price(memberShipPlans.getPrice())
                        .endDate(memberShipPlans.getStartDate().toLocalDate())
                        .TimeInDay(memberShipPlans.getTimeInDay())
                        .build()));
    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> addMembershipPlan(PlansRequest plansRequest) {
//        if (membershipPlansRepository.existsByName(plansRequest.getName())) {
//            return CompletableFuture.completedFuture(new ResponseObject("Fail", HttpStatus.BAD_REQUEST, "Membership Plan is already exist"));
//        }
        // chung name nhuwng khac gia tien => 2 service khac nhau.

        MemberShipPlans memberShipPlans = MemberShipPlans.builder()
                .name(plansRequest.getName())
                .price(plansRequest.getPrice())
                .description(plansRequest.getDescription())
                .startDate(plansRequest.getStartedDate().atTime(6, 0))
                .endDate(plansRequest.getEndDate().atTime(21, 0))
                .TimeInDay(plansRequest.getTimeInDay())
                .isActive(true).build();
        membershipPlansRepository.save(memberShipPlans);
        return CompletableFuture.completedFuture(new ResponseObject("Success", HttpStatus.OK, memberShipPlans));
    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> updateMembershipPlan(String id, PlansRequest plansRequest) {
        MemberShipPlans memberShipPlans = membershipPlansRepository.findById(id).orElse(null);

        if (memberShipPlans != null && memberShipPlans.isActive()) {
            memberShipPlans.setName(plansRequest.getName());
            memberShipPlans.setDescription(plansRequest.getDescription());
            memberShipPlans.setPrice(plansRequest.getPrice());
            memberShipPlans.setTimeInDay(plansRequest.getTimeInDay());
            memberShipPlans.setEndDate(plansRequest.getEndDate().atTime(6, 0));
            memberShipPlans.setStartDate(plansRequest.getStartedDate().atTime(21, 0));
            membershipPlansRepository.saveAndFlush(memberShipPlans);
            return CompletableFuture.completedFuture(new ResponseObject("Success", HttpStatus.OK, "Update Membership Plan successfully"));
        }
        return CompletableFuture.completedFuture(new ResponseObject("Fail", HttpStatus.BAD_REQUEST, "Membership Plan is not exist"));
    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> deleteMembershipPlan(String id) {
        MemberShipPlans memberShipPlans = membershipPlansRepository.findById(id).orElse(null);

        if (memberShipPlans != null && memberShipPlans.isActive()) {
            memberShipPlans.setActive(false);
            membershipPlansRepository.save(memberShipPlans);
            return CompletableFuture.completedFuture(new ResponseObject("Success", HttpStatus.OK, "Delete Membership Plan successfully"));
        }
        return CompletableFuture.completedFuture(new ResponseObject("Fail", HttpStatus.BAD_REQUEST, "Membership Plan is deleted"));

    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> getCategori(LocalDate localDate) {
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59, 999999999); // 23:59:59

        List<SchedulesIO> schedulesIOS = scheduleIORepository.findAllByDateBetween(startOfDay, endOfDay);

        List<GetUserCategory> getUserCategories = schedulesIOS.stream()
                .filter(schedulesIO -> schedulesIO.getTimeCheckin() != null) // Chỉ giữ lại bản ghi có giá trị
                .map(schedulesIO -> GetUserCategory.builder()
                        .idPt(schedulesIO.getTrainer() != null ? schedulesIO.getTrainer().getId() : "")
                        .checkin(schedulesIO.getTimeCheckin().toLocalTime()) // Không còn lo bị null
                        .checkout(schedulesIO.getTimeCheckout() != null ? schedulesIO.getTimeCheckout().toLocalTime() : null)
                        .id(schedulesIO.getMembers() != null && schedulesIO.getMembers().getUser() != null
                                ? schedulesIO.getMembers().getUser().getId()
                                : "")
                        .build())
                .toList();


        return CompletableFuture.completedFuture(ResponseObject.builder()
                .httpStatus(HttpStatus.OK)
                .message("Success")
                .data(getUserCategories)
                .build());
    }


}
