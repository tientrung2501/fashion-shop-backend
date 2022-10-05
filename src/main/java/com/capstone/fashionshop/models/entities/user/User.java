package com.capstone.fashionshop.models.entities.user;

import com.capstone.fashionshop.models.entities.order.Order;
import com.capstone.fashionshop.models.enums.EGender;
import com.capstone.fashionshop.models.enums.EProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Size(max = 50)
    @Email(message = "Email invalidate")
    @Indexed(unique = true)
    private String email;
    @NotNull(message = "Password can not be null")
    @Size( min = 5, max = 50)
    @JsonIgnore
    private String password;
    private String phone;
    private int province;
    private int district;
    private int ward;
    private String address;
    @NotBlank(message = "Role is required")
    private String role;
    private String avatar;
    private EGender gender;
    private EProvider provider;
    @NotBlank(message = "State is required")
    private String state;
    @ReadOnlyProperty
    @DocumentReference(lookup="{'user':?#{#self._id} }")
    @JsonIgnore
    private List<Order> orders;
    @JsonIgnore
    private Token token;
    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime createdDate;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @LastModifiedDate
    LocalDateTime lastModifiedDate;

    public User(String name, String email, String password, String phone, Integer province, Integer district, Integer ward, String address, String role, String avatar, EGender gender, String state, EProvider provider) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.address = address;
        this.role = role;
        this.avatar = avatar;
        this.gender = gender;
        this.state = state;
        this.provider = provider;
    }
}
