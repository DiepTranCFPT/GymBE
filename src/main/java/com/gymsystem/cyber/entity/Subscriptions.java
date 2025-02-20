package com.gymsystem.cyber.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

@SuperBuilder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table("subscriptions")
public class Subscriptions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Members members;

    @OneToOne(mappedBy = "subscriptions")
    @JoinColumn(name = "plan_id")
    private MemberShipPlans memberShipPlans;

    @OneToOne(mappedBy = "subscriptions")
    @JoinColumn(name = "payment_id")
    private Payment payment;
}