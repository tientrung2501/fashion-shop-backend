package com.capstone.fashionshop.models.entities.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDetail {
    private String receiveName;
    private String receivePhone;
    private String receiveAddress;
}

