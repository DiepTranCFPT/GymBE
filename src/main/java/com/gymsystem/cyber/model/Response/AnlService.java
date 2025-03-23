package com.gymsystem.cyber.model.Response;

import lombok.Builder;

@Builder
public record AnlService(String id, double total, String name, int totalUser) {
}
