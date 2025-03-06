package com.gymsystem.cyber.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.mapping.Table;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "members")
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String name ;

    private String decription;

    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    private Double price ;

    @Min(value = 1,message = "Duration must be greater than or equal to 1")
    private Integer duration;

    private LocalDateTime dateTime ;

    private LocalDateTime expireDate;

    private boolean exprire ;

    @OneToMany(mappedBy = "members",cascade = CascadeType.ALL)
    private List<SchedulesIO> schedulesIO;

    @OneToOne(mappedBy = "members")
    private Subscriptions subscriptions;

    public void addSchedulesIO(SchedulesIO schedulesIO) {
        this.schedulesIO.add(schedulesIO);
    }

}

