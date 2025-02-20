package com.gymsystem.cyber.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
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

    @OneToOne
    private Subscriptions subscriptions;

}
