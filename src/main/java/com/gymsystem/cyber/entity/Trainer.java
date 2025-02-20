package com.gymsystem.cyber.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String specialization;

    @Min(0)
    @Max(100)
    private int experience_year;

    private boolean locked = false;

}
