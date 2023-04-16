package com.capstone.fashionshop.services.recommend;

import org.springframework.http.ResponseEntity;

public interface IRecommendService {
    ResponseEntity<?> recommendProduct(String id);
}
