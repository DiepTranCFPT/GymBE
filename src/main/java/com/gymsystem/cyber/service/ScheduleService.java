package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.SchedulesIO;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.iService.IScheduleService;
import com.gymsystem.cyber.model.Response.ScheduleIORespone;
import com.gymsystem.cyber.model.Response.ScheduleinDayRespone;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.ScheduleIORepository;
import com.gymsystem.cyber.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ScheduleService implements IScheduleService {
    private final ScheduleIORepository scheduleIORepository;
    private final AccountUtils accountUtils;

    public ScheduleService(ScheduleIORepository scheduleIORepository, AccountUtils accountUtils) {
        this.scheduleIORepository = scheduleIORepository;
        this.accountUtils = accountUtils;
    }



    @Override
    public CompletableFuture<ResponseObject> getSchedule() {
        User user = accountUtils.getCurrentUser();
        List<SchedulesIO> schedulesIOS = scheduleIORepository.findByMembers(user.getMembers());
        if(schedulesIOS.isEmpty()){
            ResponseObject responseObject = new ResponseObject("User dont have Schedule",HttpStatus.FORBIDDEN,null);
            return CompletableFuture.completedFuture(responseObject);
        }
        List<ScheduleIORespone> scheduleIOResponeList = new ArrayList<>();

        for (SchedulesIO schedulesIO : schedulesIOS) {
            ScheduleIORespone scheduleIORespone1 = ScheduleIORespone.builder()
                    .dateTime(schedulesIO.getDate())
                    .timeCheckin(schedulesIO.getTimeCheckin())
                    .build();

            scheduleIOResponeList.add(scheduleIORespone1);
        }
        ResponseObject responseObject = new ResponseObject();
        responseObject.setData(scheduleIOResponeList);

        return CompletableFuture.completedFuture(responseObject);
    }

    @Override
    public CompletableFuture<ResponseObject> getScheduleinDay() {
        int dateTime = LocalDateTime.now().getDayOfYear();
        List<SchedulesIO> schedulesIO = scheduleIORepository.findAll();
        List<ScheduleinDayRespone> scheduleinDayRespones = new ArrayList<>();
        for (SchedulesIO schedulesIO1 : schedulesIO){
            if(schedulesIO1.getTimeCheckin().getDayOfYear() == dateTime){
                ScheduleinDayRespone scheduleinDayRespone = new ScheduleinDayRespone();
                scheduleinDayRespone.setCheckin(schedulesIO1.getTimeCheckin());
                scheduleinDayRespone.setCheckout(schedulesIO1.getTimeCheckout());
                scheduleinDayRespone.setName(schedulesIO1.getMembers().getUser().getName());
                scheduleinDayRespone.setNamePlans(schedulesIO1.getMembers().getName());
                scheduleinDayRespones.add(scheduleinDayRespone);
            }
        }
        return CompletableFuture.completedFuture(
                ResponseObject.builder().
                        httpStatus(HttpStatus.OK).
                        data(scheduleinDayRespones).
                        message("OK").build());
    }

}
