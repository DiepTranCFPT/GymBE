package com.gymsystem.cyber.entity;

import jakarta.persistence.*;


@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @OneToOne
    @JoinColumn(name = "subscription_id")
    private Subscriptions subscriptions;

    private double amount;

    private String paymentMethod;

    private Boolean status;

    private String createAt;
}
