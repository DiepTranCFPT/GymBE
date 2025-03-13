package com.gymsystem.cyber.model.Response;

import lombok.Builder;

@Builder
public record TrainerReponse (String id,
                              String name,
                              String email,
                              String phone,
                              boolean enable,
                              boolean status,
                              int experience_year,
                              byte[] avata) {
}
