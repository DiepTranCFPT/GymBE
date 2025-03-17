package com.gymsystem.cyber.controller;

import com.gymsystem.cyber.iService.IScheduleService;
import com.gymsystem.cyber.model.ResponseObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final IScheduleService iScheduleService;


    public ScheduleController(IScheduleService iScheduleService) {
        this.iScheduleService = iScheduleService;
    }

    @GetMapping("getSchedule")
    public CompletableFuture<ResponseObject> getScheduleByUser(){
       return iScheduleService.getSchedule();
    }
    @GetMapping("getSchedule-day")
    public CompletableFuture<ResponseObject> getScheduleInDay(){
        return iScheduleService.getScheduleinDay();
    }

}
