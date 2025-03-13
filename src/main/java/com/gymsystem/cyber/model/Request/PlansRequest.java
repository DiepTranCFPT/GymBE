package com.gymsystem.cyber.model.Request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PlansRequest {

  private String id;

  private  String name;

  private String description;

  private Double price;

  private LocalDate startedDate;

  private LocalDate endDate;

  private int TimeInDay;
}
