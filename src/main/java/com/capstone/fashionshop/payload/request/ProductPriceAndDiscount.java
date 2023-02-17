package com.capstone.fashionshop.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceAndDiscount {
    @NotBlank(message = "Product id is required")
    private String id;
    private BigDecimal price = null;
    @Range(min = -1, max = 100, message = "Invalid discount! Only from 0 to 100")
    private int discount = -1;
}
