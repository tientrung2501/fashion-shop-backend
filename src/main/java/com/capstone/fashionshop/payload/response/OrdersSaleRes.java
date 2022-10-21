package com.capstone.fashionshop.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersSaleRes {
    private String date;
    private BigDecimal amount;
    private int quantity;
}
