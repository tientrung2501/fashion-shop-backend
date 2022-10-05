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
public class PaymentDetail {
    private String paymentId;
    private String paymentType;
    private String paymentToken;
    private Map<String, Object> paymentInfo = new HashMap<>();
}
