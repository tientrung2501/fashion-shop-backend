package com.capstone.fashionshop.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShippingItemReq {
    private String name;
    private int quantity;
}
