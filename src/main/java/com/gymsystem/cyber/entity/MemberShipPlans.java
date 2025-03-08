package com.gymsystem.cyber.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Entity
@Table(name = "membership_plans")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MemberShipPlans {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    private String name;

    private String description;

    private Double price;

    private LocalDateTime startDate = LocalDate.now().atTime(6, 0);

    private LocalDateTime endDate = LocalDate.now().atTime(6, 0);

    private boolean isActive;

    // thoi gia toi da cua 1 service (tuy loai co tg khac nhau)
    private int timeInDay;

    @OneToMany(mappedBy = "memberShipPlans", fetch = FetchType.EAGER)
    private List<Subscriptions> subscriptions;

}
