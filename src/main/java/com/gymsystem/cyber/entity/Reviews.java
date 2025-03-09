package com.gymsystem.cyber.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

import java.lang.reflect.Member;
import java.util.List;

@SuperBuilder
@Entity
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "reviews")
    private List<SchedulesIO> schedulesIO;
}
