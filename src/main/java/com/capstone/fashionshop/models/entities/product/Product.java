package com.capstone.fashionshop.models.entities.product;

import com.capstone.fashionshop.models.entities.Brand;
import com.capstone.fashionshop.models.entities.Category;
import com.capstone.fashionshop.models.entities.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.TextScore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "products")
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;
    @NotBlank(message = "Name is required")
    @Indexed(unique = true)
    @TextIndexed(weight = 10)
    private String name;
    @NotBlank(message = "Description is required")
    @TextIndexed(weight = 1)
    private String description;
    @NotNull(message = "Price is required")
    private BigDecimal price;
    @NotNull(message = "Discount is required")
    @Range(min = 0, max = 100, message = "Invalid discount! Only from 0 to 100")
    private int discount = 0;
    @NotBlank(message = "Category is required")
    @DocumentReference
    private Category category;
    @NotBlank(message = "Brand is required")
    @DocumentReference
    private Brand brand;
    private double rate = 0;
    @TextIndexed(weight = 8)
    private List<ProductAttribute> attr = new ArrayList<>();
    @NotBlank(message = "State is required")
    private String state;
    @ReadOnlyProperty
    @DocumentReference(lookup="{'product':?#{#self._id} }", lazy = true)
    private List<ProductOption> productOptions;
//    @ReadOnlyProperty
//    @DocumentReference(lookup="{'product':?#{#self._id} }", lazy = true)
    private List<ProductImage> images = new ArrayList<>();
    @ReadOnlyProperty
    @DocumentReference(lookup="{'product':?#{#self._id} }", lazy = true)
    private List<Review> reviews;
    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime createdDate;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @LastModifiedDate
    LocalDateTime lastModifiedDate;
    @TextScore
    Float score;

    public Product(String name, String description, BigDecimal price, Category category, Brand brand, String state, int discount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.state = state;
        this.discount = discount;
    }

    @Transient
    public int getRateCount() {
        try {
            return reviews.size();
        } catch (Exception e) {
            return 0;
        }
    }
}
