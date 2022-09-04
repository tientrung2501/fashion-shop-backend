package com.capstone.fashionshop.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CartRes {
    private String id;
    private long totalProduct = 0;
    private BigDecimal totalPrice;
    private List<CartItemRes> items = new ArrayList<>();
    private String state;

    public CartRes(String id, long totalProduct, BigDecimal totalPrice, String state) {
        this.id = id;
        this.totalProduct = totalProduct;
        this.totalPrice = totalPrice;
        this.state = state;
    }
}
