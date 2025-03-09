package com.gymsystem.cyber.iService;

import com.gymsystem.cyber.model.ResponseObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IScheduleService {
    CompletableFuture<ResponseObject> getSchedule();

}
