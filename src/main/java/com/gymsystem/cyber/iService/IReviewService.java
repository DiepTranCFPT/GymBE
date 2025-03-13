package com.gymsystem.cyber.iService;

import com.gymsystem.cyber.model.Request.ReviewRequest;
import com.gymsystem.cyber.model.ResponseObject;
import org.springframework.stereotype.Service;


public interface IReviewService {

    ResponseObject createNewReview(ReviewRequest reviewRequest);

    // READ: Lấy danh sách review của user hoặc theo id review
    ResponseObject getReviewsByTrainer(String userId);

    ResponseObject getReviewById(String reviewId);

    // UPDATE: Cập nhật review
    ResponseObject updateReview(String reviewId, ReviewRequest reviewRequest);

    // DELETE: Xóa review
    ResponseObject deleteReview(String reviewId);
}
