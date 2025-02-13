package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PracticeInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime timeUpdatedLast;

    private Boolean deleted;

    private LocalDateTime timeCreated;

    private float calo;

    private LocalDateTime dateTime;

    private String description;

    private int time;

    @ManyToOne
    @JoinColumn(name = "pt_idP", referencedColumnName = "id", insertable = false, updatable = false)
    private User pt;

    @ManyToOne
    @JoinColumn(name = "calendar_idP", referencedColumnName = "id", insertable = false, updatable = false)
    private Calendar calendar;


}

