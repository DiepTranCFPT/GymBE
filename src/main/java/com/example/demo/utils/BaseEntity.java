package com.example.demo.utils;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedSuperclass // Đánh dấu lớp này để Hibernate ánh xạ các trường vào bảng con
public class BaseEntity {
    public String createBy;
    public String lastUpdate;
    public String deletedBy;
    public LocalDateTime lastUpdatedTime;
    public LocalDateTime deletedTime;
    public LocalDateTime createTime;


}
