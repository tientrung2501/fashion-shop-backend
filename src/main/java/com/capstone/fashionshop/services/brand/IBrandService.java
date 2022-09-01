package com.capstone.fashionshop.services.brand;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IBrandService {
    ResponseEntity<?> findAll();
    ResponseEntity<?> findBrandById(String id);
    ResponseEntity<?> addBrand(String name, MultipartFile file);
    ResponseEntity<?> updateBrand(String id, String name, MultipartFile file);
    ResponseEntity<?> deactivatedBrand(String id);
}
