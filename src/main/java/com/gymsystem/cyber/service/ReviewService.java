package com.gymsystem.cyber.service;

import com.gymsystem.cyber.entity.Reviews;
import com.gymsystem.cyber.entity.SchedulesIO;
import com.gymsystem.cyber.entity.User;
import com.gymsystem.cyber.iService.IReviewService;
import com.gymsystem.cyber.model.Request.ReviewRequest;
import com.gymsystem.cyber.model.Response.ReviewRepo;
import com.gymsystem.cyber.model.ResponseObject;
import com.gymsystem.cyber.repository.MembershipPlansRepository;
import com.gymsystem.cyber.repository.ReviewRepository;
import com.gymsystem.cyber.repository.ScheduleIORepository;
import com.gymsystem.cyber.utils.AccountUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        reviewRepository.save(reviews);

        return ResponseObject.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(reviews)
                .build();
    }

    @Override
    @Transactional
    public ResponseObject getReviewsByTrainer(String userId) {
        List<Reviews> reviewsList = reviewRepository.findByTrainer_Id(userId);
        List<ReviewRepo> reviewRepos = reviewsList.stream()
                .map(reviews -> ReviewRepo.builder()
                        .id(reviews.getId())
                        .id(reviews.getId())
                        .schedulesIoID(reviews.getSchedulesIO().stream().map(SchedulesIO::getId).collect(Collectors.toList()))
                        .rating(reviews.getRating())
                        .build())
                .collect(Collectors.toUnmodifiableList());
        return ResponseObject.builder()
                .httpStatus(HttpStatus.OK)
                .message("OK")
                .data(reviewRepos)
                .build();
    }

    @Override
    @Transactional
    public ResponseObject getReviewById(String reviewId) {
        Reviews review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null) {

            ReviewRepo reviewRepo = ReviewRepo.builder()
                    .id(review.getId())
                    .idTrainer(review.getTrainer().getId())
                    .schedulesIoID(review.getSchedulesIO().stream().map(SchedulesIO::getId).collect(Collectors.toUnmodifiableList()))
                    .rating(review.getRating())
                    .build();

            return ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("OK")
                    .data(reviewRepo)
                    .build();
        }
        return ResponseObject.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("Review not found.")
                .build();
    }

    @Override
    @Transactional
    public ResponseObject updateReview(String reviewId, ReviewRequest reviewRequest) {
        Optional<Reviews> existingReview = reviewRepository.findById(reviewId);
        if (existingReview.isPresent()) {
            Reviews review = existingReview.get();
            review.setComment(reviewRequest.getDescription());
            review.setRating(reviewRequest.getRating());
            reviewRepository.save(review);

            return ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Review updated successfully.")
                    .data(true)
                    .build();
        }
        return ResponseObject.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("Review not found.")
                .data(false)
                .build();
    }

    @Override
    @Transactional
    public ResponseObject deleteReview(String reviewId) {
        Optional<Reviews> review = reviewRepository.findById(reviewId);
        if (review.isPresent()) {
            review.get().setDeleted(true);
            reviewRepository.save(review.get());
            return ResponseObject.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Review deleted successfully.")
                    .data(true)
                    .build();
        }
        return ResponseObject.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message("Review not found.")
                .data(false)
                .build();
    }
}
