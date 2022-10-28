package com.capstone.fashionshop.services.category;

import com.capstone.fashionshop.payload.request.CategoryReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ICategoryService {
    ResponseEntity<?> findAll();
    ResponseEntity<?> findRoot(Boolean root);
    ResponseEntity<?> findCategoryById(String id);
    ResponseEntity<?> addCategory(CategoryReq req);
    ResponseEntity<?> updateCategory(String id, CategoryReq req);
    ResponseEntity<?> updateCategoryImage(String id, MultipartFile file);
    ResponseEntity<?> deactivatedCategory(String id);
}
