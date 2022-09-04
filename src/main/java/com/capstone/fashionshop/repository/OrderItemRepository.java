package com.capstone.fashionshop.repository;

import com.capstone.fashionshop.models.entities.order.OrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderItemRepository extends MongoRepository<OrderItem, String> {
}
