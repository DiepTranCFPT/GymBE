package com.gymsystem.cyber.model.Response;


import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MemberPlans(String id, LocalDate datestart, LocalDate enddate, double price, int timeinday, String name) {}