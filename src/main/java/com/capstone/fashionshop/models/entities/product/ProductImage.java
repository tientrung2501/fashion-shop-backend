package com.capstone.fashionshop.models.entities.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "product_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    @Id
    private String id;
    private String url;
    private boolean thumbnail;
    private String color;
    @DocumentReference(lazy = true)
    @JsonIgnore
    private Product product;

    public ProductImage(String url, boolean thumbnail, String color, Product product) {
        this.url = url;
        this.thumbnail = thumbnail;
        this.color = color;
        this.product = product;
    }
}
