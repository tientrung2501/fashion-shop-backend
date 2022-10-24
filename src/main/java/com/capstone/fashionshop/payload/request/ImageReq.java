package com.capstone.fashionshop.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ImageReq {
    String color;
    String imageId;
    List<MultipartFile> files;
}
