package com.capstone.fashionshop.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
public class ShippingReq {
    private Long province_id;
    private Long district_id;
}
