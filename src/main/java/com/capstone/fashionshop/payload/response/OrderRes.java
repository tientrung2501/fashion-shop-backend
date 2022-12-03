package com.capstone.fashionshop.payload.response;

import com.capstone.fashionshop.models.entities.order.DeliveryDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderRes {
    private String id;
    private String userId;
    private String userName;
    private long totalProduct = 0;
    private BigDecimal totalPrice;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CartItemRes> items = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String paymentType;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private DeliveryDetail deliveryDetail;
    private String state;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdDate;

    public OrderRes(String id, String userId, String userName, long totalProduct, BigDecimal totalPrice, String state, LocalDateTime createdDate) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.totalProduct = totalProduct;
        this.totalPrice = totalPrice;
        this.state = state;
        this.createdDate = createdDate;
    }
}
