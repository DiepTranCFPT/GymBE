package com.gymsystem.cyber.model.Response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DetaileUserinSystem(String id,MembersRC members,MemberPlans memberPlans ) {
}