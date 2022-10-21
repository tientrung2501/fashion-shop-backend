package com.capstone.fashionshop.models.entities;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.models.entities.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Document(collection = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private String id;
    @NotBlank(message = "Name is required")
    @Indexed(unique = true)
    @TextIndexed(weight = 9)
    private String name;
    private String image;
    private boolean root = true;
    private String state;
    @DocumentReference
    private List<Category> subCategories;
    @DocumentReference(lookup="{'category':?#{#self._id} }", lazy = true)
    @JsonIgnore
    private List<Product> products;

    public Category(String name, String image, String state) {
        this.name = name;
        this.image = image;
        this.state = state;
    }

    public List<Category> getSubCategories() {
        subCategories.removeIf(category -> (category.getState().equals(Constants.DISABLE)));
        return subCategories;
    }
}
