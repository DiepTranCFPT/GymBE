package com.gymsystem.cyber.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import org.springframework.data.relational.core.mapping.Table;

@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("schedules_io")
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

    private int time;

    private String activity;

    private boolean status;
}
