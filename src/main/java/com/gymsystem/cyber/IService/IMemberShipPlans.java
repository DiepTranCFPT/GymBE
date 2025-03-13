package com.gymsystem.cyber.iService;

import com.gymsystem.cyber.model.Request.PlansRequest;
import com.gymsystem.cyber.model.ResponseObject;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;


public interface IMemberShipPlans {
    CompletableFuture<ResponseObject> getMembershipPlans();

    CompletableFuture<ResponseObject> getMembershipPlan(String id);

    CompletableFuture<ResponseObject> addMembershipPlan(PlansRequest plansRequest);

    CompletableFuture<ResponseObject> updateMembershipPlan(String name, PlansRequest plansRequest);

    CompletableFuture<ResponseObject> deleteMembershipPlan(String name);

    CompletableFuture<ResponseObject> getCategori(LocalDate localDate);
}