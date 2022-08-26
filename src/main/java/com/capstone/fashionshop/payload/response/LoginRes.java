package com.capstone.fashionshop.payload.response;

import com.capstone.fashionshop.models.enums.EGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRes {
    private String email;
    private String name;
    private String avatar;
    private EGender gender;
    private String role;
    private String accessToken;
}
