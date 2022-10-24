package com.capstone.fashionshop.services.product;

import com.capstone.fashionshop.models.entities.product.ProductAttribute;
import com.capstone.fashionshop.payload.request.ProductReq;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    ResponseEntity<?> findAll(boolean isAdmin, Pageable pageable);
    ResponseEntity<?> findById(String id);
    ResponseEntity<?> findByCategoryIdOrBrandId(String catId, Pageable pageable);
    ResponseEntity<?> search(String key, Pageable pageable);
    ResponseEntity<?> addProduct(ProductReq req);
    ResponseEntity<?> updateProduct(String id, ProductReq req);
    ResponseEntity<?> deactivatedProduct(String id);
    ResponseEntity<?> destroyProduct(String id);
    ResponseEntity<?> addAttribute(String id, ProductAttribute req);
    ResponseEntity<?> updateAttribute(String id, ProductAttribute req);
    ResponseEntity<?> deleteAttribute(String id, String name);
    ResponseEntity<?> addImagesToProduct(String id, String color, List<MultipartFile> files);
    ResponseEntity<?> deleteImageFromProduct(String id, String imageId);
}