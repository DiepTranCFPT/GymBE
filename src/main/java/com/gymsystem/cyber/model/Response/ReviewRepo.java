package com.gymsystem.cyber.model.Response;

import lombok.Builder;

import java.util.List;

@Builder
public record ReviewRepo(String id,
                         String idTrainer,
                         List<String> schedulesIoID,
                         String rating) {
}
