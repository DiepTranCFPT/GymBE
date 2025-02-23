package com.gymsystem.cyber.iService;

import com.gymsystem.cyber.model.Request.PlansRequest;
import com.gymsystem.cyber.model.ResponseObject;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.concurrent.CompletableFuture;

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public interface IMemberShipPlans {

    CompletableFuture<ResponseObject> getMembershipPlans();
    CompletableFuture<ResponseObject> getMembershipPlan(String name);
    CompletableFuture<ResponseObject> addMembershipPlan(PlansRequest plansRequest);
    CompletableFuture<ResponseObject> updateMembershipPlan(String name, PlansRequest plansRequest);
    CompletableFuture<ResponseObject> deleteMembershipPlan(String name);
}
