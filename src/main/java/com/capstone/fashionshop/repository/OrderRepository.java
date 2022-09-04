package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.order.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Order> findOrderByUser_IdAndState(ObjectId userId, String state);
    List<Order> findAllByState(String state);
}
