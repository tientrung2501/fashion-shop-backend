package com.capstone.fashionshop.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRes {
    private String itemId;
    private String name;
    private int discount;
    private String image;
    private BigDecimal price;
    private String productOptionId;
    private String color;
    private String size;
    private long quantity;
    private long stock;
    private BigDecimal subPrice;
    private boolean reviewed;
}

