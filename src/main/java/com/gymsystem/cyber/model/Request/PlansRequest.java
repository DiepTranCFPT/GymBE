package com.gymsystem.cyber.model.Request;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlansRequest {
  private  String name;

  private String description;

  private Double price;

  private LocalDateTime startedDate;

  private LocalDateTime endDate;
}
