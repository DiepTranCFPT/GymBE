package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.MemberShipPlans;
import com.gymsystem.cyber.iService.IMemberShipPlans;
import com.gymsystem.cyber.model.Request.PlansRequest;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.MembershipPlansRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MembershipPlanService implements IMemberShipPlans {

    private final MembershipPlansRepository membershipPlansRepository;

    public MembershipPlanService(MembershipPlansRepository membershipPlansRepository) {
        this.membershipPlansRepository = membershipPlansRepository;
    }

    @Override
    public CompletableFuture<ResponseObject> getMembershipPlans() {
        List<MemberShipPlans> memberShipPlans = membershipPlansRepository.findAll();
        return CompletableFuture.completedFuture(new ResponseObject("Success", HttpStatus.OK, memberShipPlans));
    }

    @Override
    public CompletableFuture<ResponseObject> getMembershipPlan(String name) {
        MemberShipPlans memberShipPlans = membershipPlansRepository.findByName(name);
        if(memberShipPlans == null){
            return CompletableFuture.completedFuture(new ResponseObject("Fail", HttpStatus.BAD_REQUEST, "Membership Plan is not exist"));
        }
        return CompletableFuture.completedFuture(new ResponseObject("Success", HttpStatus.OK, memberShipPlans));
    }

    @Override
    public CompletableFuture<ResponseObject> addMembershipPlan(PlansRequest plansRequest) {
        if(membershipPlansRepository.existsByName(plansRequest.getName())){
            return CompletableFuture.completedFuture(new ResponseObject("Fail", HttpStatus.BAD_REQUEST, "Membership Plan is already exist"));
        }
        MemberShipPlans memberShipPlans = MemberShipPlans.builder()
                .name(plansRequest.getName())
                .price(plansRequest.getPrice())
                .description(plansRequest.getDescription())
                .startDate(plansRequest.getStartedDate())
                .endDate(plansRequest.getEndDate())
                .isActive(true).build();
        membershipPlansRepository.save(memberShipPlans);
        return CompletableFuture.completedFuture(new ResponseObject("Success", HttpStatus.OK, memberShipPlans));
    }

    @Override
    public CompletableFuture<ResponseObject> updateMembershipPlan(String name, PlansRequest plansRequest) {
        MemberShipPlans memberShipPlans = membershipPlansRepository.findByName(name);
        if(memberShipPlans != null && memberShipPlans.isActive()){
            MemberShipPlans.builder()
                    .name(plansRequest.getName())
                    .price(plansRequest.getPrice())
                    .description(plansRequest.getDescription())
                    .startDate(plansRequest.getStartedDate())
                    .endDate(plansRequest.getEndDate())
                    .isActive(memberShipPlans.isActive()).build();
            membershipPlansRepository.saveAndFlush(memberShipPlans);
            return CompletableFuture.completedFuture(new ResponseObject("Success", HttpStatus.OK, "Update Membership Plan successfully"));
        }
        return CompletableFuture.completedFuture(new ResponseObject("Fail", HttpStatus.BAD_REQUEST, "Membership Plan is not exist"));
    }

    @Override
    public CompletableFuture<ResponseObject> deleteMembershipPlan(String name) {
        MemberShipPlans memberShipPlans = membershipPlansRepository.findByName(name);

        if (memberShipPlans != null && !memberShipPlans.isActive()) {
            memberShipPlans.setActive(false);
            membershipPlansRepository.save(memberShipPlans);
            return CompletableFuture.completedFuture(new ResponseObject("Success", HttpStatus.OK, "Delete Membership Plan successfully"));
        }
        return  CompletableFuture.completedFuture(new ResponseObject("Fail", HttpStatus.BAD_REQUEST, "Membership Plan is deleted"));

    }
}
