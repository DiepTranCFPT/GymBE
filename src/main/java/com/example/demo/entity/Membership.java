package com.example.demo.entity;

import com.example.demo.enums.MembershipType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime timeUpdatedLast;

    private Boolean deleted;

    private LocalDateTime timeCreated;

    private LocalDate startDate;

    private LocalDate endDate;

    private MembershipType membershipType;




    @OneToMany
    @JoinColumn(name = "bill_idM", referencedColumnName = "id", insertable = false, updatable = false)
    private List<Bill> bill;

    @ManyToOne
    @JoinColumn(name = "service_idM", referencedColumnName = "id", insertable = false, updatable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "user_idM", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    // Getter and Setter methods
}

