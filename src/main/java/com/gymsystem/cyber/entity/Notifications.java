package com.gymsystem.cyber.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String status;

    private String createAt;

    private LocalDateTime dateTime = LocalDateTime.now();

}
