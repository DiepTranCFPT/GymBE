package com.gymsystem.cyber.iService;
import com.gymsystem.cyber.model.ResponseObject;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;


public interface ISchedulesService {
    CompletableFuture<ResponseObject> countMembersByDay(LocalDateTime date);
    CompletableFuture<ResponseObject> countMembers(int month, int year);
}
