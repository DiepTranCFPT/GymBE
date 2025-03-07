package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.Members;
import com.gymsystem.cyber.entity.Reviews;
import com.gymsystem.cyber.entity.SchedulesIO;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.iService.IReviewService;
import com.gymsystem.cyber.model.Request.ReviewRequest;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.MembershipPlansRepository;
import com.gymsystem.cyber.repository.ReviewRepository;
import com.gymsystem.cyber.repository.ScheduleIORepository;
import com.gymsystem.cyber.utils.AccountUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService implements IReviewService {
    private final AccountUtils accountUtils;
    private final ScheduleIORepository scheduleIORepository;
    private final ReviewRepository reviewRepository;




    public ReviewService(AccountUtils accountUtils, ScheduleIORepository scheduleIORepository, MembershipPlansRepository membershipPlansRepository, ReviewRepository reviewRepository) {
        this.accountUtils = accountUtils;
        this.scheduleIORepository = scheduleIORepository;

        this.reviewRepository = reviewRepository;
    }


    @Override
    public ResponseObject createNewReview(ReviewRequest reviewRequest) {
        User user = accountUtils.getCurrentUser();
        List<SchedulesIO> list = scheduleIORepository.findByMembers(user.getMembers());

        Optional<SchedulesIO> check1 = list.stream()
                .filter(schedulesIO -> schedulesIO.getDate().getDayOfYear() == LocalDateTime.now().getDayOfYear()
                        && schedulesIO.getDate().getMonth() == LocalDateTime.now().getMonth())
                .findFirst();

        if (!check1.isPresent()) {
            return ResponseObject.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("No matching schedule found for today.")
                    .build();
        }

        Reviews reviews = Reviews.builder()
                .createAt(user.getName())
                .comment(reviewRequest.getDescription())
                .rating(reviewRequest.getRating())
                .trainer(check1.get().getTrainer())
                .build();

        // Save the review
        reviewRepository.save(reviews);

        return ResponseObject.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(reviews)
                .build();
    }
    }

