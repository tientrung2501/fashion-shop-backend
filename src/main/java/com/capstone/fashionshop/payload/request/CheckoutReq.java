package com.capstone.fashionshop.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutReq {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Phone is required")
    private String phone;
    @NotBlank(message = "Province can not be null")
    private String province;
    @NotBlank(message = "District can not be null")
    private String district;
    @NotBlank(message = "Ward can not be null")
    private String ward;
    @NotBlank(message = "Address is required")
    private String address;
    private Long shippingFee;
    private Integer serviceType;
    private Long expectedDeliveryTime = 0L;
}
