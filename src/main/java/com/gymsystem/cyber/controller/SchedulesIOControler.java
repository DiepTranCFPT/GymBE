package com.gymsystem.cyber.controller;


import com.gymsystem.cyber.iService.ISchedulesIOService;
import com.gymsystem.cyber.model.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
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


}
