package com.gymsystem.cyber.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;
import org.springframework.data.relational.core.mapping.Table;

@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "schedules_io")
public class SchedulesIO {

    @Id
    protected String id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Members members;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    private LocalDateTime date;

    private LocalDateTime timeCheckin;

    private LocalDateTime timeCheckout;

    private String activity;

    private int Time;

    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reviews reviews;
}
