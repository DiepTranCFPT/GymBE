package com.example.demo.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String specialization;

    @Size(min = 1)
    private int experience_year;

    private boolean availability;
}
