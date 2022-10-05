package com.capstone.fashionshop.services.order;

import com.capstone.fashionshop.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    @Override
    public ResponseEntity<?> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public ResponseEntity<?> findOrderById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> cancelOrder(String id) {
        return null;
    }
}
