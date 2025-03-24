package com.gymsystem.cyber.controller;


import com.gymsystem.cyber.iService.ISchedulesIOService;
import com.gymsystem.cyber.model.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Controller
@RestController
@RequestMapping("api/schedules-io")
public class SchedulesIOControler {


    private final ISchedulesIOService iSchedulesIOService;


    public SchedulesIOControler(ISchedulesIOService iSchedulesIOService) {
        this.iSchedulesIOService = iSchedulesIOService;
    }


    @GetMapping("/user/{id}")
    @Operation(summary = "Lay lich tap cua user")
    public CompletableFuture<ResponseObject> getScheduleByIdUserId(@PathVariable("id") String id) {
        return iSchedulesIOService.getScheduleByIdUserId(id);
    }


    @GetMapping("/user/{id}/list-booking")
    @Operation(summary = "lay danh sach booking Pt trong thang ")
    public CompletableFuture<ResponseObject> getScheduleListBooking(@PathVariable("id") String id, @RequestParam(value = "date", required = false) LocalDate date) {
        return iSchedulesIOService.getListCategoryPtDate(id, date);
    }


    @GetMapping("/category/total/{date}")
    @Operation(summary = "lay lich theo tung category (thang) (ADMIN) (2025-05-24)")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CompletableFuture<ResponseObject> getTotalCategoryPt(@PathVariable("date") LocalDate localDate) {
        return iSchedulesIOService.getScheduleByDateTime(localDate);
    }

    @GetMapping("/category/total-day/{date}")
    @Operation(summary = "lay lich theo tung category (ngay) (ADMIN) (2025-05-24)")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CompletableFuture<ResponseObject> getTotalCategorywithDay(@PathVariable("date") LocalDate localDate) {
        return iSchedulesIOService.getListCategoryWithDate(localDate);
    }

}
