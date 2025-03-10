package com.gymsystem.cyber.controller;

import com.gymsystem.cyber.iService.IReviewService;
import com.gymsystem.cyber.model.Request.ReviewRequest;
import com.gymsystem.cyber.model.ResponseObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }
    @PostMapping
    public ResponseObject createReview(@RequestBody ReviewRequest reviewRequest) {
        return reviewService.createNewReview(reviewRequest);
    }

    @GetMapping("/{reviewId}")
    public ResponseObject getReviewById(@PathVariable String reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @GetMapping("/trainer/{userId}")
    public ResponseObject getReviewsByTrainer(@PathVariable String userId) {
        return reviewService.getReviewsByTrainer(userId);
    }

    @PutMapping("/{reviewId}")
    public ResponseObject updateReview(@PathVariable String reviewId, @RequestBody ReviewRequest reviewRequest) {
        return reviewService.updateReview(reviewId, reviewRequest);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseObject deleteReview(@PathVariable String reviewId) {
        return reviewService.deleteReview(reviewId);
    }
}
