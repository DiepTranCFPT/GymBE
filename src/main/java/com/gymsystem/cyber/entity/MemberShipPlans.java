package com.gymsystem.cyber.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class MemberShipPlans {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    private String name;

    private String description;

    private double price;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

}
