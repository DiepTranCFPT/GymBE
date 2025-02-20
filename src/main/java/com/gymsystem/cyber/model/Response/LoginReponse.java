package com.gymsystem.cyber.model.Response;

import lombok.Builder;

@Builder
public record LoginReponse(String name,
                           String email,
                           String phone,
                           byte[] avata,
                           String token) {
}
