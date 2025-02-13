package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime timeUpdatedLast;

    private Boolean deleted;

    private LocalDateTime timeCreated;

    private int quantity;

    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "membership_idB", referencedColumnName = "id", insertable = false, updatable = false)
    private Membership membership;

    @ManyToOne
    @JoinColumn(name = "payment_idB", referencedColumnName = "id", insertable = false, updatable = false)
    private Payment payment;

    @OneToMany
    @JoinColumn(name = "service_idB", referencedColumnName = "id", insertable = false, updatable = false)
    private List<Service> service;

    @ManyToOne
    @JoinColumn(name = "user_idB", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    // Getter and Setter methods
}

