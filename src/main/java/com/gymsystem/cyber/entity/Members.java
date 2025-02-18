package com.gymsystem.cyber.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



@Entity
public class Members {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String name;

    private String decription;

    @Size(min = 0)
    private double price;

    @Size(min = 1, max = 31)
    private int duration;


}

