package com.capstone.fashionshop.services.order;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IOrderService {
    ResponseEntity<?> findAll(Pageable pageable);
    ResponseEntity<?> findOrderById(String id);
    ResponseEntity<?> cancelOrder(String id);
}
