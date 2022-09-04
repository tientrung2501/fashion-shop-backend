package com.capstone.fashionshop.mapper;

import com.capstone.fashionshop.models.entities.order.Order;
import com.capstone.fashionshop.models.entities.order.OrderItem;
import com.capstone.fashionshop.payload.response.CartItemRes;
import com.capstone.fashionshop.payload.response.CartRes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartMapper {
    public CartRes toCartRes (Order order) {
        CartRes res = new CartRes(order.getId(), order.getTotalProduct(), order.getTotalPrice(), order.getState());
        res.setItems(order.getItems().stream().map(CartMapper::toCartItemRes).collect(Collectors.toList()));
        return res;
    }

    public static CartItemRes toCartItemRes(OrderItem orderItem) {
        return new CartItemRes(orderItem.getId(), orderItem.getItem().getProduct().getName(),
                orderItem.getItem().getProduct().getDiscount(),
                orderItem.getItem().getVariants().get(0).getImages().get(0).getUrl(),
                orderItem.getItem().getProduct().getPrice().add(orderItem.getItem().getExtraFee()),
                orderItem.getItem().getId(), orderItem.getColor(), orderItem.getItem().getName(),
                orderItem.getQuantity(), orderItem.getItem().getVariants().get(0).getStock(), orderItem.getSubPrice());
    }
}
