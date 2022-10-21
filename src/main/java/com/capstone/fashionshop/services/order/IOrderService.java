package com.capstone.fashionshop.services.order;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IOrderService {
    ResponseEntity<?> findAll(String state, Pageable pageable);
    ResponseEntity<?> findOrderById(String id);
    ResponseEntity<?> findOrderById(String id, String userId);
    ResponseEntity<?> cancelOrder(String id, String userId);
    ResponseEntity<?> getOrderStatistical(String from, String to, String type);
}
