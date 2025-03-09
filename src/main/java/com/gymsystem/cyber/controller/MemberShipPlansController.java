package com.gymsystem.cyber.controller;

import com.gymsystem.cyber.iService.IMemberShipPlans;
import com.gymsystem.cyber.model.Request.PlansRequest;
import com.gymsystem.cyber.model.ResponseObject;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.concurrent.CompletableFuture;

@Controller
@RestController
@RequestMapping("/membership-plans")
public class MemberShipPlansController {
    private final IMemberShipPlans membershipPlansService;

    public MemberShipPlansController(IMemberShipPlans membershipPlansService) {
        this.membershipPlansService = membershipPlansService;
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public CompletableFuture<ResponseObject> addMembershipPlan(@RequestBody PlansRequest membershipPlanRequest) {
        return membershipPlansService.addMembershipPlan(membershipPlanRequest);
    }

    @GetMapping
    public CompletableFuture<ResponseObject> getAllMembershipPlans() {
        return membershipPlansService.getMembershipPlans();
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseObject> getMembershipPlan(String name) {
        return membershipPlansService.getMembershipPlan(name);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public CompletableFuture<ResponseObject> updateMembershipPlan( String name, @RequestBody PlansRequest membershipPlanRequest) {
        return membershipPlansService.updateMembershipPlan(name, membershipPlanRequest);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseObject> deleteMembershipPlan( String name) {
        return membershipPlansService.deleteMembershipPlan(name);
    }
}
