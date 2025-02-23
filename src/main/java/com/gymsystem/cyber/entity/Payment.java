package com.gymsystem.cyber.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table("payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @OneToOne(mappedBy = "payment")
    private Subscriptions subscriptions;

    private Double amount;

    private String paymentMethod;

    private Boolean status;

    private LocalDateTime createAt;

}
