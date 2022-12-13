package com.capstone.fashionshop.utils;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.models.entities.order.Order;
import com.capstone.fashionshop.repository.OrderRepository;
import com.capstone.fashionshop.services.payment.PaymentUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
@Getter
@Setter
public class PaymentValidatorUtils implements Runnable {
    private OrderRepository orderRepository;
    private String orderId;
    private PaymentUtils paymentUtils;

    @Override
    @Async
    @Transactional
    public void run() {
        log.info("Start checking payment timeout!");
        if (!orderId.isBlank()) {
            Optional<Order> order = orderRepository.findOrderByIdAndState(orderId, Constants.ORDER_STATE_PROCESS);
            if (order.isPresent()) {
                try {
                    if (new Date(System.currentTimeMillis() - Constants.PAYMENT_TIMEOUT).after(
                            (Date) order.get().getPaymentDetail().getPaymentInfo()
                                    .get("orderDate"))) {
                        String check = paymentUtils.checkingUpdateQuantityProduct(order.get(), false);
                        log.info("Restock is " + (check == null));
                        order.get().setState(Constants.ORDER_STATE_CANCEL);
                        orderRepository.save(order.get());
                        log.info("Checking payment successful!");
                    } else log.warn("Time is remaining");
                } catch (Exception e) {
                    log.error(e.getMessage());
                    log.error("failed to save order when checking payment timeout!");
                }
            }
        } else log.error("Order id in checking payment timeout is blank!");
        log.info("Checking payment timeout end!");
    }
}
