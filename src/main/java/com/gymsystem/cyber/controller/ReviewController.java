package com.gymsystem.cyber.controller;

import com.gymsystem.cyber.iService.IReviewService;
import com.gymsystem.cyber.model.Request.ReviewRequest;
import com.gymsystem.cyber.model.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseObject createReview(@RequestBody ReviewRequest reviewRequest){
        reviewService.createNewReview(reviewRequest);
        return ResponseObject.builder().httpStatus(HttpStatus.OK).message("Create Success").build();
    }
}
