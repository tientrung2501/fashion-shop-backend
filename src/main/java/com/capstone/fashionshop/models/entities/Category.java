package com.capstone.fashionshop.models.entities;

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

    public Category(String name, String image, String state) {
        this.name = name;
        this.image = image;
        this.state = state;
    }
}
