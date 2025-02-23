package com.gymsystem.cyber.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Entity
@Table("membership_plans")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MemberShipPlans {

    @Id
    protected String id;

    private String name;

    private String description;

    private Double price;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean isActive;

    @OneToOne(mappedBy = "memberShipPlans")
    @JoinColumn(name = "subscription_id")
    private Subscriptions subscriptions;

}
