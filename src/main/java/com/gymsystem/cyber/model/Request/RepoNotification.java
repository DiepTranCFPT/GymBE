package com.gymsystem.cyber.model.Request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RepoNotification(String id, String header, String content, LocalDateTime dateTime) {
}
