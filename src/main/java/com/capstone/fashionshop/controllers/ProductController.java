package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.models.entities.product.ProductAttribute;
import com.capstone.fashionshop.payload.request.ProductReq;
import com.capstone.fashionshop.services.product.IProductService;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ProductController {
    private final IProductService productService;

    @GetMapping(path = "/products/{id}")
    public ResponseEntity<?> findById (@PathVariable("id") String id){
        return productService.findById(id);
    }

    @GetMapping(path = "/products/category/{id}")
    public ResponseEntity<?> findByCategoryIdAndBrandId (@PathVariable("id") String id,
                                                         @ParameterObject Pageable pageable){
        return productService.findByCategoryIdAndBrandId(id, pageable);
    }

    @GetMapping(path = "/manage/products")
    public ResponseEntity<?> findAll (@ParameterObject Pageable pageable){
        return productService.findAll(pageable);
    }

    @PostMapping("/manage/products")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductReq req) {
        return productService.addProduct(req);
    }

    @PutMapping("/manage/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") String id,
                                           @Valid @RequestBody ProductReq req) {
        return productService.updateProduct(id, req);
    }

    @DeleteMapping("/manage/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id) {
        return productService.deactivatedProduct(id);
    }

    @DeleteMapping("/manage/products/destroy/{id}")
    public ResponseEntity<?> destroyProduct(@PathVariable("id") String id) {
        return productService.destroyProduct(id);
    }

    @PostMapping("/manage/products/attribute/{productId}")
    public ResponseEntity<?> addAttribute(@PathVariable("productId") String id ,
                                          @Valid @RequestBody ProductAttribute req) {
        return productService.addAttribute(id, req);
    }

    @PutMapping("/manage/products/attribute/{productId}")
    public ResponseEntity<?> updateAttribute(@PathVariable("productId") String id,
                                           @Valid @RequestBody ProductAttribute req) {
        return productService.updateAttribute(id, req);
    }

    @DeleteMapping("/manage/products/attribute/{productId}")
    public ResponseEntity<?> deleteAttribute(@PathVariable("productId") String id,
                                             @RequestBody ProductAttribute req) {
        return productService.deleteAttribute(id, req.getName());
    }
}
