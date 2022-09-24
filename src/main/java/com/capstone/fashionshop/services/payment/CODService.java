package com.capstone.fashionshop.services.payment;

import com.capstone.fashionshop.models.entities.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class CODService extends PaymentFactory{
    @Override
    public ResponseEntity<?> createPayment(HttpServletRequest request, Order order) {
        return null;
    }

    @Override
    public ResponseEntity<?> executePayment(String paymentId, String payerId, String responseCode, String id, HttpServletResponse response) {
        return null;
    }

    @Override
    public ResponseEntity<?> cancelPayment(String id, String responseCode, HttpServletResponse response) {
        return null;
    }
}
