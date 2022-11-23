package com.capstone.fashionshop.models.entities;

import com.capstone.fashionshop.models.entities.product.Product;
import com.capstone.fashionshop.models.entities.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document(collection = "reviews")
@Data
@NoArgsConstructor
public class Review {
    @Id
    private String id;
    @NotBlank(message = "Content is required")
    private String content;
    @NotNull(message = "Rate is required")
    private double rate;
    @DocumentReference(lazy = true)
    @JsonIgnore
    @Indexed
    private Product product;
    @DocumentReference(lazy = true)
    @JsonIgnore
    @Indexed
    private User user;
    private boolean enable;
    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime createdDate;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @LastModifiedDate
    LocalDateTime lastModifiedDate;

    public Review(String content, double rate, Product product, User user, boolean enable) {
        this.content = content;
        this.rate = rate;
        this.product = product;
        this.user = user;
        this.enable = enable;
    }
}
