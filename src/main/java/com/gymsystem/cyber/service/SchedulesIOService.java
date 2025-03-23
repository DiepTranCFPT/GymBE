package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.Members;
import com.gymsystem.cyber.entity.SchedulesIO;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.iService.ISchedulesIOService;
import com.gymsystem.cyber.model.Response.BaseUser;
import com.gymsystem.cyber.model.Response.CalendarTotal;
import com.gymsystem.cyber.model.Response.SchedulesIORepo;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.AuthenticationRepository;
import com.gymsystem.cyber.repository.ScheduleIORepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class SchedulesIOService implements ISchedulesIOService {

    private final ScheduleIORepository scheduleIORepository;
    private final AuthenticationRepository authenticationRepository;


    public SchedulesIOService(ScheduleIORepository scheduleIORepository, AuthenticationRepository authenticationRepository) {
        this.scheduleIORepository = scheduleIORepository;
        this.authenticationRepository = authenticationRepository;
    }


    @Override
    @Transactional
    public CompletableFuture<ResponseObject> getScheduleByIdUserId(String userId) {

        User user = authenticationRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        Members members = user.getMembers();

        Optional.ofNullable(members)
                .map(Members::getExpireDate)
                .filter(expireDate -> expireDate.isBefore(LocalDateTime.now()))
                .ifPresent(expireDate -> {
                    throw new UsernameNotFoundException("User is expired");
                });

        List<SchedulesIO> schedulesIOS = members.getSchedulesIO();
        if (schedulesIOS == null || schedulesIOS.isEmpty()) {
            throw new UsernameNotFoundException("No schedules found");
        }

        List<SchedulesIORepo> schedulesIORepos = schedulesIOS.stream().map(schedulesIO -> SchedulesIORepo.builder()
                .id(schedulesIO.getId())
                .checkin(schedulesIO.getTimeCheckin() != null)
                .checkout(schedulesIO.getTimeCheckout() != null)
                .TrainerId(schedulesIO.getTrainer() != null ? schedulesIO.getTrainer().getId() : "")
                .dateTime(schedulesIO.getDate().toLocalDate())
                .build()
        ).collect(Collectors.toUnmodifiableList());


        return CompletableFuture.completedFuture(ResponseObject.builder()
                .data(schedulesIORepos)
                .message("Success")
                .httpStatus(HttpStatus.OK)
                .build());
    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> getScheduleByDateTime(LocalDate dateTime) {
        List<SchedulesIO> schedulesIOS = scheduleIORepository.findAllByDateBetween(
                        dateTime.withDayOfMonth(1).atTime(6, 0),
                        dateTime.withDayOfMonth(dateTime.lengthOfMonth()).atTime(21, 0)
                ).stream()
                .filter(schedulesIO -> schedulesIO.getTimeCheckin() != null)
                .collect(Collectors.toList());

        // Chuyển đổi thành CalendarTotal
        List<CalendarTotal> calendarTotals = schedulesIOS.stream()
                .map(schedulesIO -> CalendarTotal.builder()
                        .localDate(schedulesIO.getDate().toLocalDate())
                        .baseUsers(List.of(BaseUser.builder()
                                .id(schedulesIO.getMembers().getUser().getId())
                                .Checkin(schedulesIO.getTimeCheckin().toLocalTime())
                                .Checkout(schedulesIO.getTimeCheckout() != null
                                        ? schedulesIO.getTimeCheckout().toLocalTime()
                                        : null)
                                .ptMail(schedulesIO.getTrainer() != null ? schedulesIO.getTrainer().getUser().getEmail() : "")
                                .build()))
                        .build())
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(ResponseObject.builder()
                .message("List total User in: " + dateTime)
                .data(calendarTotals)
                .httpStatus(HttpStatus.OK)
                .build());
    }


    @Override
    public CompletableFuture<ResponseObject> getUserUserInM() {
        return null;
    }

    @Override
    public CompletableFuture<ResponseObject> getUserUserInDate() {
        return null;
    }

    @Override
    public CompletableFuture<ResponseObject> getPTFreeTime(LocalDate dateTime) {
        return null;
    }

    @Override
    @Transactional
    public CompletableFuture<ResponseObject> getListCategoryPtDate(String userid, LocalDate dateTime) {

        List<SchedulesIO> schedulesIOS = scheduleIORepository
                .findAllByMembers_User_IdAndTrainerIsNotNullAndDateBetween(
                        userid,
                        dateTime.withDayOfMonth(1).atStartOfDay(), // Ngày đầu tháng: 1 lúc 00:00:00
                        dateTime.withDayOfMonth(dateTime.lengthOfMonth()).atTime(LocalTime.MAX) // Ngày cuối tháng: 28, 30 hoặc 31 lúc 23:59:59.999999999
                );


        List<SchedulesIORepo> schedulesIORepos = schedulesIOS.stream()
                .map(schedulesIO -> SchedulesIORepo.builder()
                        .dateTime(schedulesIO.getDate().toLocalDate())
                        .id(schedulesIO.getId())
                        .checkout(schedulesIO.getTimeCheckout() != null)
                        .checkin(schedulesIO.getTimeCheckin() != null)
                        .TrainerId(schedulesIO.getTrainer() != null ? schedulesIO.getTrainer().getId() : "")
                        .build())
                .collect(Collectors.toList());


        return CompletableFuture.completedFuture(ResponseObject.builder()
                .data(schedulesIORepos)
                .httpStatus(HttpStatus.OK)
                .message("Success")
                .build());
    }
}
