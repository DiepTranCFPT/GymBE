package com.gymsystem.cyber.controller;


import com.gymsystem.cyber.iService.ISchedulesService;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.service.SchedulesService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/Schedules")
public class SchedulesController {
    private final ISchedulesService schedulesService;


    public SchedulesController(SchedulesService schedulesService) {
        this.schedulesService = schedulesService;
    }
    @Operation(summary = "Lấy số lương người dùng theo ngày", description = "Đảm bảo rằng bạn truyền giá trị theo định dạng yyyy-MM-dd")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/daily-count")
    public CompletableFuture<ResponseObject> getDailyCount(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        return schedulesService.countMembersByDay(startOfDay);
    }

    @Operation(summary = "Lấy số lương người dùng theo tháng")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/monthly-count")
    public CompletableFuture<ResponseObject> getMonthlyCount(@RequestParam("month") int month, @RequestParam("year") int year) {
        return schedulesService.countMembers(month, year);
    }
}
