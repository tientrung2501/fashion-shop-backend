package com.capstone.fashionshop.services.review;

import com.capstone.fashionshop.payload.request.ReviewReq;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IReviewService {
    ResponseEntity<?> findByProductId(String productId ,Pageable pageable);
    ResponseEntity<?> addReview(String userId , ReviewReq req);
}
