package com.capstone.fashionshop.models.entities.product;

import com.capstone.fashionshop.models.entities.Brand;
import com.capstone.fashionshop.models.entities.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String id;
    @NotBlank(message = "Name is required")
    @Indexed(unique = true)
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Price is required")
    private BigDecimal price;
    @NotBlank(message = "Category is required")
    @DocumentReference
    private Category category;
    @NotBlank(message = "Brand is required")
    @DocumentReference
    private Brand brand;
    private String url;
    private List<ProductAttribute> attr;
    @NotBlank(message = "State is required")
    private String state;
    @ReadOnlyProperty
    @DocumentReference(lookup="{'product':?#{#self._id} }")
    private List<ProductOption> productOptions;
    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime createdDate;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @LastModifiedDate
    LocalDateTime lastModifiedDate;

    public Product(String name, String description, BigDecimal price, Category category, Brand brand, String state) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.state = state;
    }
}
