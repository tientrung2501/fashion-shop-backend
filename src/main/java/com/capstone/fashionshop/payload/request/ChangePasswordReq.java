package com.capstone.fashionshop.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class ChangePasswordReq {
    @NotBlank(message = "Old Password is required")
    @Size( min = 5, max = 50)
    private String oldPassword;
    @NotBlank(message = "New Password is required")
    @Size( min = 5, max = 50)
    private String newPassword;
}
