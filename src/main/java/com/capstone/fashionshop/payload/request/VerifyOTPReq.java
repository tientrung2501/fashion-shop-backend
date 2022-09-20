package com.capstone.fashionshop.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class VerifyOTPReq {
    @NotBlank(message = "OTP is required")
    @Size( min = 6, max = 6)
    private String otp;
    @NotBlank(message = "Email is required")
    @Size( min = 5, max = 50)
    @Email(message = "Email invalidate")
    private String email;
    @NotBlank(message = "Type is required")
    private String type;
}
