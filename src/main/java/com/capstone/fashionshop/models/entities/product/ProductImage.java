package com.capstone.fashionshop.models.entities.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductImage {
    private String url;
    private boolean thumbnail;
}
