package com.capstone.fashionshop.payload.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductReq {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Price is required")
    private BigDecimal price;
    @NotNull(message = "Discount is required")
    @Range(min = 0, max = 100, message = "Invalid discount! Only from 0 to 100")
    private int discount;
    @NotBlank(message = "Category is required")
    private String category;
    @NotBlank(message = "Brand is required")
    private String brand;
}
