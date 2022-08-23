package com.capstone.fashionshop.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter@Setter@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
}
