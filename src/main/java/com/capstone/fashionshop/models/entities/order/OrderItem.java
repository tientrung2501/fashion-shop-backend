package com.capstone.fashionshop.models.entities.order;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.models.entities.product.ProductOption;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

@Document(collection = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    private String id;
    @DocumentReference
    @Indexed
    private ProductOption item;
    @NotBlank
    private String color;
    @NotNull
    private long quantity;
    @DocumentReference(lazy = true)
    @JsonIgnore
    @Indexed
    private Order order;
    @Field(targetType = DECIMAL128)
    private BigDecimal price = BigDecimal.ZERO;
    private boolean reviewed = false;
    @Transient
    private BigDecimal subPrice = BigDecimal.ZERO;

    public ProductOption getItem() {
        item.setVariants(item.getVariants().stream()
                .filter(v -> v.getColor().equals(color)).collect(Collectors.toList()));
        return item;
    }

    public BigDecimal getSubPrice() {
        if (order.getState().equals(Constants.ORDER_STATE_ENABLE)) {
            BigDecimal originPrice = (item.getProduct().getPrice().add(item.getExtraFee())).multiply(BigDecimal.valueOf(quantity));
            String discountString = originPrice.multiply(BigDecimal.valueOf((double) (100- item.getProduct().getDiscount())/100))
                    .stripTrailingZeros().toPlainString();
            return new BigDecimal(discountString);
        } else return price.multiply(BigDecimal.valueOf(quantity));
    }

    public OrderItem(ProductOption item, String color, long quantity, Order order) {
        this.item = item;
        this.color = color;
        this.quantity = quantity;
        this.order = order;
    }
}
