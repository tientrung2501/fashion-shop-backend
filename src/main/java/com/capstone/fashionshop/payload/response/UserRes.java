package com.capstone.fashionshop.payload.response;

import com.capstone.fashionshop.models.enums.EGender;
import lombok.Data;

@Data
public class UserRes {
    private String id;
    private String email;
    private String name;
    private String avatar;
    private String phone;
    private Integer province;
    private Integer district;
    private Integer ward;
    private String address;
    private EGender gender;
    private String role;
    private String state;
}
