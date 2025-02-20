package com.gymsystem.cyber.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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