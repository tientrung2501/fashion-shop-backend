package com.capstone.fashionshop.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryReq {
    @NotBlank(message = "Name is required")
    private String name;
    private String parent_category = "-1";
    private MultipartFile file;
    private String state;
}
