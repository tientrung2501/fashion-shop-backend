package com.capstone.fashionshop.models.entities.order;

import com.capstone.fashionshop.models.entities.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String id;
    @DocumentReference(lazy = true)
    @JsonIgnore
    private User user;
    @ReadOnlyProperty
    @DocumentReference(lookup="{'order':?#{#self._id} }")
    private List<OrderItem> items = new ArrayList<>();
    @NotBlank(message = "State is required")
    private String state;
    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime createdDate;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @LastModifiedDate
    LocalDateTime lastModifiedDate;
    @Transient
    private long totalProduct = 0;
    @Transient
    private BigDecimal totalPrice;

    public long getTotalProduct() {
        return items.size();
    }

    public BigDecimal getTotalPrice() {
        totalPrice = items.stream().map(OrderItem::getSubPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalPrice;
    }

    public Order(User user, String state) {
        this.user = user;
        this.state = state;
    }
}
