package com.gymsystem.cyber.entity;

import jakarta.persistence.*;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Table("reviews")
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_ID")
    private Trainer trainer;

    private String rating;

    private String comment;

    private String createAt;

}
