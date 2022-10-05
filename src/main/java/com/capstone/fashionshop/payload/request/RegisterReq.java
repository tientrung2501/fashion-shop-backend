package com.capstone.fashionshop.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RegisterReq {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Size(max = 50)
    @Email(message = "Email invalidate")
    private String email;
    @NotBlank(message = "Password can not be null")
    @Size( min = 5, max = 50)
    private String password;
    @NotBlank(message = "Phone can not be null")
    private String phone;
    @NotNull(message = "Province can not be null")
    private Integer province;
    @NotNull(message = "District can not be null")
    private Integer district;
    @NotNull(message = "Ward can not be null")
    private Integer ward;
    @NotBlank(message = "Address can not be null")
    private String address;
    @NotBlank(message = "Gender can not be null")
    private String gender;
    private String role;
}
