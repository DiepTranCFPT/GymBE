package com.gymsystem.cyber.iService;

import com.gymsystem.cyber.model.Request.ReviewRequest;
import com.gymsystem.cyber.model.ResponseObject;
import org.springframework.stereotype.Service;


public interface IReviewService {

    ResponseObject createNewReview(ReviewRequest reviewRequest);
}
