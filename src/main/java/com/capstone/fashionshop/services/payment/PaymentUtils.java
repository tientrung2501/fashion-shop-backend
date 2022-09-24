package com.capstone.fashionshop.services.payment;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.models.entities.order.Order;
import com.capstone.fashionshop.models.entities.order.OrderItem;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.repository.OrderRepository;
import com.capstone.fashionshop.repository.ProductOptionRepository;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentUtils {
    private final ProductOptionRepository productOptionRepository;
    private final OrderRepository orderRepository;

    @Synchronized
    @Transactional
    public String checkingUpdateQuantityProduct(Order order, boolean isPaid) {
        order.getItems().forEach(item -> {
            item.getItem().getVariants().forEach(i -> {
                if (isPaid) {
                    if (item.getColor().equals(i.getColor()) && i.getStock() < item.getQuantity()) {
                        order.setState(Constants.ORDER_STATE_ENABLE);
                        orderRepository.save(order);
                        throw new AppException(HttpStatus.CONFLICT.value(),
                                "Quantity exceeds the available stock on hand at Product:" +
                                        item.getItem().getProduct().getName());
                    } else i.setStock(i.getStock() - item.getQuantity());
                } else i.setStock(i.getStock() + item.getQuantity());
            });
            productOptionRepository.save(item.getItem());
        });
        return null;
    }
}
