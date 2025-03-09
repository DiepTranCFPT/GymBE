package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.SchedulesIO;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.iService.IScheduleService;
import com.gymsystem.cyber.model.Response.ScheduleIORespone;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.ScheduleIORepository;
import com.gymsystem.cyber.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

}
