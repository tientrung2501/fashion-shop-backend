package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.order.Order;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Order> findOrderByUser_IdAndState(ObjectId userId, String state);
    Page<Order> findAllByState(String state, Pageable pageable);
    Optional<Order> findOrderByPaymentDetail_PaymentTokenAndState(String token, String state);
    Page<Order> findAllByCreatedDateBetweenAndState(LocalDateTime from, LocalDateTime to, String state, Pageable pageable);
}
