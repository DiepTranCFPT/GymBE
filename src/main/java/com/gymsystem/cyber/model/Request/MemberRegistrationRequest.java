package com.gymsystem.cyber.model.Request;

import lombok.Data;

@Data
public class MemberRegistrationRequest {
    private String name;
    private double price;
    private int duration;
    private String description;
}
