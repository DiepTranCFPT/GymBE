package com.gymsystem.cyber.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class SchedulesIO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members members;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    private LocalDateTime date;

    private int time;

    private String activity;

    private boolean status;
}
