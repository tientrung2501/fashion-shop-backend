package com.capstone.fashionshop.mapper;

import com.capstone.fashionshop.models.entities.order.Order;
import com.capstone.fashionshop.payload.response.OrderRes;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class OrderMapper {
    public OrderRes toOrderRes (Order order) {
        return new OrderRes(order.getId(), order.getUser().getId(), order.getUser().getName(),
                order.getTotalProduct(), order.getTotalPrice(), order.getState(), order.getLastModifiedDate());
    }

    public OrderRes toOrderDetailRes (Order order) {
        OrderRes orderRes =  new OrderRes(order.getId(), order.getUser().getId(), order.getUser().getName(),
                order.getTotalProduct(), order.getTotalPrice(), order.getState(), order.getLastModifiedDate());
        orderRes.setItems(order.getItems().stream().map(CartMapper::toCartItemRes).collect(Collectors.toList()));
        orderRes.setPaymentType(order.getPaymentDetail().getPaymentType());
        orderRes.setPaymentInfo(order.getPaymentDetail().getPaymentInfo());
        orderRes.setDeliveryDetail(order.getDeliveryDetail());
        return orderRes;
    }
}
