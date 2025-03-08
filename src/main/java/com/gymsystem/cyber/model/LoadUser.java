package com.gymsystem.cyber.model;

import lombok.Builder;

@Builder
public record LoadUser(String id, byte[] avata) {
}
