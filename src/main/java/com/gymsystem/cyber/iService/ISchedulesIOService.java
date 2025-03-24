package com.gymsystem.cyber.iService;

import com.google.type.DateTime;
import com.gymsystem.cyber.model.ResponseObject;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public interface ISchedulesIOService {
    CompletableFuture<ResponseObject> getScheduleByIdUserId(String userId);

    CompletableFuture<ResponseObject> getScheduleByDateTime(LocalDate dateTime);

    CompletableFuture<ResponseObject> getUserUserInM();

    CompletableFuture<ResponseObject> getUserUserInDate();

    CompletableFuture<ResponseObject> getPTFreeTime(LocalDate dateTime);

    CompletableFuture<ResponseObject> getListCategoryPtDate(String userid, LocalDate dateTime);

    CompletableFuture<ResponseObject> getListCategoryWithDate(LocalDate dateTime);


}
