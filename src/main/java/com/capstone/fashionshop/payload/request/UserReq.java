package com.capstone.fashionshop.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UserReq {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Phone can not be null")
    private String phone;
    @NotBlank(message = "Address can not be null")
    private String address;
    @NotBlank(message = "Gender can not be null")
    private String gender;
    private String password;
}
