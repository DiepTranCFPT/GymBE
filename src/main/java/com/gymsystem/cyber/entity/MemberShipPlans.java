package com.gymsystem.cyber.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Entity
@Table("membership_plans")
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
