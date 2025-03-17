package com.gymsystem.cyber.model.Response;

import lombok.Builder;

@Builder
public record PtRepo(String id, String email, String name, int kn) {
}
