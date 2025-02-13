package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime timeUpdatedLast;

    private Boolean deleted;

    private LocalDateTime timeCreated;

    private double amount;

    private String billId;

    @ManyToOne
    @JoinColumn(name = "bill_idP", referencedColumnName = "id", insertable = false, updatable = false)
    private Bill bill;

    // Getter and Setter methods
}
