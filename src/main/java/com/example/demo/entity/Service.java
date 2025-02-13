package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime timeUpdatedLast;

    private Boolean deleted;

    private LocalDateTime timeCreated;

    private String description;

    private byte[] img;

    private Boolean isBlock;

    private String name;

    private double price;

    private int valueTimeout;


    @ManyToOne
    @JoinColumn(name = "membership_idS", referencedColumnName = "id", insertable = false, updatable = false)
    private Membership membership;

    // Getter and Setter methods
}

