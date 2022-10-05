package com.capstone.fashionshop.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserReq {
    @NotBlank(message = "Name is required")
    private String name;
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
    private String password;
}
