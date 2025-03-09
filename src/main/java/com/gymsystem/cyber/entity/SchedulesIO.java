package com.gymsystem.cyber.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.cglib.core.Local;
import jakarta.persistence.Table;

@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "schedules_io", indexes = {@Index(name = "idx_member_id", columnList = "member_id")
        , @Index(name = "idx_trainer_id", columnList = "trainer_id")})
public class SchedulesIO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Members members;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    private LocalDateTime date;

    private LocalDateTime timeCheckin;

    private LocalDateTime timeCheckout;

    private int time;

    private String activity;

    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reviews reviews;
}
