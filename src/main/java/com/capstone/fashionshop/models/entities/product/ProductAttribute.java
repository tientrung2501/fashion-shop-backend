package com.capstone.fashionshop.models.entities.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ProductAttribute {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Value is required")
    private String val;
}
