package com.capstone.fashionshop.models.entities.order;

import com.capstone.fashionshop.models.entities.product.ProductOption;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Document(collection = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    private String id;
    @DocumentReference
    private ProductOption item;
    @NotBlank
    private String color;
    @NotNull
    private long quantity;
    @DocumentReference(lazy = true)
    @JsonIgnore
    private Order order;
    @Transient
    private BigDecimal subPrice = BigDecimal.ZERO;

    public ProductOption getItem() {
        item.setVariants(item.getVariants().stream()
                .filter(v -> v.getColor().equals(color)).collect(Collectors.toList()));
        return item;
    }

    public BigDecimal getSubPrice() {
        BigDecimal originPrice = (item.getProduct().getPrice().add(item.getExtraFee())).multiply(BigDecimal.valueOf(quantity));
        String discountString = originPrice.multiply(BigDecimal.valueOf((double) (100- item.getProduct().getDiscount())/100))
                .stripTrailingZeros().toPlainString();
        return new BigDecimal(discountString);
    }

    public OrderItem(ProductOption item, String color, long quantity, Order order) {
        this.item = item;
        this.color = color;
        this.quantity = quantity;
        this.order = order;
    }
}
