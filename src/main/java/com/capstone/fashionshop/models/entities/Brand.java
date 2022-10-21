package com.capstone.fashionshop.models.entities;

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

import java.util.List;

@Document(collection = "brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    @Id
    private String id;
    @Indexed(unique = true)
    @TextIndexed(weight = 9)
    private String name;
    private String image;
    private String state;
    @DocumentReference(lookup="{'brand':?#{#self._id} }", lazy = true)
    @JsonIgnore
    private List<Product> products;

    public Brand(String name, String image, String state) {
        this.name = name;
        this.image = image;
        this.state = state;
    }
}
