package com.gymsystem.cyber.controller;

import com.gymsystem.cyber.iService.IMemberShipPlans;
import com.gymsystem.cyber.model.Request.PlansRequest;
import com.gymsystem.cyber.model.ResponseObject;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Controller
@RestController
@RequestMapping("api/membership-plan")
public class MemberShipPlansController {
    private final IMemberShipPlans membershipPlansService;

    public MemberShipPlansController(IMemberShipPlans membershipPlansService) {
        this.membershipPlansService = membershipPlansService;
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add-plan")
    @Operation(summary = "tao moi menberplan (ADMIN)")
    public CompletableFuture<ResponseObject> addMembershipPlan(@RequestBody PlansRequest membershipPlanRequest) {
        return membershipPlansService.addMembershipPlan(membershipPlanRequest);
    }

    @GetMapping("/all")
    @Operation(summary = " lấy tất cả các dịch vụ có trên hệ thống với nội dung công khai") // sửa
    public CompletableFuture<ResponseObject> getAllMembershipPlans() {
        return membershipPlansService.getMembershipPlans();
    }

    @GetMapping("/mb-plan/{id}")
    @Operation(summary = "lay memberplan voi id tuong ung")
    public CompletableFuture<ResponseObject> getMembershipPlan(String id) {
        return membershipPlansService.getMembershipPlan(id);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    @Operation(summary = "Cap nhat memberplan voi id tuong ung: (ADMIN)")
    public CompletableFuture<ResponseObject> updateMembershipPlan(@PathVariable("id") String id, @RequestBody PlansRequest membershipPlanRequest) {
        return membershipPlansService.updateMembershipPlan(id, membershipPlanRequest);
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "xoa memberplan voi id tuong ung: (ADMIN)")
    public CompletableFuture<ResponseObject> deleteMembershipPlan(@PathVariable("id") String id) {
        return membershipPlansService.deleteMembershipPlan(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/activitie")
    @Operation(summary = "lay danh sach tat ca ca user hoat dong trong ngay (2025-03-07)")
    public CompletableFuture<ResponseObject> getActivitieday(@RequestParam LocalDate date) {
        return membershipPlansService.getCategori(date);
    }



}
