package com.capstone.fashionshop.models.entities.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Document(collection = "product_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
//    @Id
    private String imageId;
    private String url;
    private boolean thumbnail;
    private String color;
//    @DocumentReference(lazy = true)
//    @JsonIgnore
//    private Product product;
}
