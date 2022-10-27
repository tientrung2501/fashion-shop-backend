package com.capstone.fashionshop.services.brand;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IBrandService {
    ResponseEntity<?> findAll();
    ResponseEntity<?> findAll(String state);
    ResponseEntity<?> findBrandById(String id);
    ResponseEntity<?> addBrand(String name, MultipartFile file);
    ResponseEntity<?> updateBrand(String id, String name, String state, MultipartFile file);
    ResponseEntity<?> deactivatedBrand(String id);
}
