package com.gymsystem.cyber.entity;

import jakarta.persistence.*;

@Entity
public class Subscriptions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members members;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private MemberShipPlans memberShipPlans;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
