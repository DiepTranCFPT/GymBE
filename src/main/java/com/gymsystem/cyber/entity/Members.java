package com.gymsystem.cyber.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
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
    private double price ;

    @Size(min = 1)
    private int duration;

    private LocalDateTime dateTime = LocalDateTime.now();

    private LocalDateTime expireDate = dateTime.plusMonths(duration);

    private boolean exprire = expireDate.isBefore(LocalDateTime.now());

    @OneToMany(mappedBy = "members")
    private List<SchedulesIO> schedulesIO = new ArrayList<>();

    @OneToOne(mappedBy = "members")
    private Subscriptions subscriptions;

    public void addSchedulesIO(SchedulesIO schedulesIO) {
        this.schedulesIO.add(schedulesIO);
    }



}

