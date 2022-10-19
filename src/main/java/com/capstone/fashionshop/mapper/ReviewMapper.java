package com.capstone.fashionshop.mapper;

import com.capstone.fashionshop.models.entities.Review;
import com.capstone.fashionshop.payload.response.ReviewRes;
import org.springframework.stereotype.Service;

@Service
public class ReviewMapper {
    public ReviewRes toReviewRes(Review req) {
        return new ReviewRes(req.getId(), req.getContent(), req.getRate(),
                req.isEnable(), req.getUser().getName(), req.getCreatedDate());
    }
}
