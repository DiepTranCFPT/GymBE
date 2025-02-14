package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_ID")
    private Trainer trainer;

    private String rating;

    private String comment;

    private String createAt;

}
