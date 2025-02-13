package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime timeUpdatedLast;

    private Boolean deleted;

    private LocalDateTime timeCreated;


    @ManyToOne
    @JoinColumn(name = "service_idC", referencedColumnName = "id", insertable = false, updatable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "user_idC", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;


}

