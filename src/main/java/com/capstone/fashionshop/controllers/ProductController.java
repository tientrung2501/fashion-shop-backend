package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.models.entities.product.ProductAttribute;
import com.capstone.fashionshop.payload.request.ImageReq;
import com.capstone.fashionshop.payload.request.ProductReq;
import com.capstone.fashionshop.services.product.IProductService;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        return productService.findByCategoryIdOrBrandId(id, pageable);
    }

    @GetMapping(path = "/products/search")
    public ResponseEntity<?> search (@RequestParam("q") String query,
                                                         @PageableDefault(sort = "score") @ParameterObject Pageable pageable){
        if (query.isEmpty() || query.matches(".*[%<>&;'\0-].*"))
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "Invalid keyword");
        return productService.search(query, pageable);
    }

    @GetMapping(path = "/products")
    public ResponseEntity<?> findAllByState (@ParameterObject Pageable pageable){
        return productService.findAll(Constants.ENABLE, pageable);
    }

    @GetMapping(path = "/manage/products")
    public ResponseEntity<?> findAll (@RequestParam(value = "state", defaultValue = "") String state,
                                      @ParameterObject Pageable pageable){
            return productService.findAll(state,pageable);
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

    @PostMapping(value = "/manage/products/images/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addImages(@PathVariable("productId") String id ,
                                          @ModelAttribute ImageReq req) {
        return productService.addImagesToProduct(id, req.getColor(), req.getFiles());
    }

    @DeleteMapping("/manage/products/images/{productId}")
    public ResponseEntity<?> deleteImage(@PathVariable("productId") String id,
                                         @RequestBody ImageReq req) {
        return productService.deleteImageFromProduct(id, req.getImageId());
    }
}
