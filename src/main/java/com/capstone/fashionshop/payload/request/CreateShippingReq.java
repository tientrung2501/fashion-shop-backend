package com.capstone.fashionshop.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateShippingReq {
    @NotBlank(message = "Province is required!")
    private String to_province_name;
    @NotBlank(message = "District is required!")
    private String to_district_name;
    @NotBlank(message = "Ward is required!")
    private String to_ward_name;
    private List<ShippingItemReq> items = new ArrayList<>();
}