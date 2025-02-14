package com.example.demo.entity;

import jakarta.persistence.*;


@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "subscription_id")
    private Subscriptions subscriptions;

    private double amount;

    private String paymentMethod;

    private Boolean status;

    private String createAt;
}
