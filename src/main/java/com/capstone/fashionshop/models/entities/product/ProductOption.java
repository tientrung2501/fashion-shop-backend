package com.capstone.fashionshop.models.entities.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Document(collection = "product_option")
@Data
@AllArgsConstructor
public class ProductOption {
    @Id
    private String id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Stock is required")
    private Long stock;
    private int discount;
    private Color color;
    private BigDecimal extraFee;
    private List<ProductImage> images;
    @DocumentReference(lazy = true)
    private Product product;
}
