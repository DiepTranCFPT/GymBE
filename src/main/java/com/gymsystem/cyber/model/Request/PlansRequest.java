package com.gymsystem.cyber.model.Request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PlansRequest {
  private  String name;

  private String description;

  private Double price;

  private LocalDateTime startedDate;

  private LocalDateTime endDate;
}
