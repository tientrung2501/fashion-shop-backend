package com.capstone.fashionshop.payload.response;

import com.capstone.fashionshop.models.entities.product.ProductAttribute;
import com.capstone.fashionshop.models.entities.product.ProductImage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductListRes {
    private String id;
    private String name;
    private String url;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private int discount;
    private String category;
    private String brand;
    private String state;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime createdDate;
    private List<ProductAttribute> attr;
    private List<ProductImage> images;
}
