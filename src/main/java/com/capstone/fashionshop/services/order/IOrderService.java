package com.capstone.fashionshop.services.order;

import com.capstone.fashionshop.payload.request.CreateShippingReq;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IOrderService {
    ResponseEntity<?> findAll(String state, Pageable pageable);
    ResponseEntity<?> findOrderById(String id);
    ResponseEntity<?> findOrderById(String id, String userId);
    ResponseEntity<?> cancelOrder(String id, String userId);
    ResponseEntity<?> createShip(CreateShippingReq req, String orderId);
    ResponseEntity<?> changeState(String state, String orderId);
    ResponseEntity<?> changeState(String state, String orderId, String userId);
}
