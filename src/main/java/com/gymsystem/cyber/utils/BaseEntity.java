package com.gymsystem.cyber.utils;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    private LocalDateTime timeCreated;

    private LocalDateTime TimeUpdatedLast;

    //    @Column(name = "is_delete")
    protected boolean deleted = false;


    // khi tao 1 doi tuong moi tg se duoc tu dong lu vao voi ngay gio he thong
    @PrePersist
    protected void createDateTime() {
        timeCreated = LocalDateTime.now();
    }


    // tu dong cap nhat tg khi obj duoc thai doi
    @PreUpdate
    protected void updateDateTime() {
        TimeUpdatedLast = LocalDateTime.now();
    }
}
