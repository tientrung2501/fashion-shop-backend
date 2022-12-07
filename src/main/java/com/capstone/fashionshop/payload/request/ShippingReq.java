package com.capstone.fashionshop.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
public class ShippingReq {
    private Integer service_type_id;
    private Long province_id;
    private Long district_id;
    private Long to_district_id;
    private String to_ward_code;
    private Long ward_code;
    private Long weight;
}
