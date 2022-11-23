package com.capstone.fashionshop.models.entities.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDetail {
    private String receiveName;
    private String receivePhone;
    private String receiveProvince;
    private String receiveDistrict;
    private String receiveWard;
    private String receiveAddress;
    private Map<String, Object> deliveryInfo = new HashMap<>();

    public DeliveryDetail(String receiveName, String receivePhone, String receiveProvince, String receiveDistrict, String receiveWard, String receiveAddress) {
        this.receiveName = receiveName;
        this.receivePhone = receivePhone;
        this.receiveProvince = receiveProvince;
        this.receiveDistrict = receiveDistrict;
        this.receiveWard = receiveWard;
        this.receiveAddress = receiveAddress;
    }
}

