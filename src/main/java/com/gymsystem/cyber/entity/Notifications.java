package com.gymsystem.cyber.entity;


import jakarta.persistence.*;

@Entity
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String status;

    private String createAt;

}
