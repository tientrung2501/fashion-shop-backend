package com.capstone.fashionshop.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class LoginReq {
    @NotBlank
    @Email(message = "Email invalidate")
    private String username;
    @NotBlank
    private String password;
}
