package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.services.brand.IBrandService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class BrandController {
    private final IBrandService brandService;

    @GetMapping(path = "/brands")
    public ResponseEntity<?> findAll (){
        return brandService.findAll();
    }

    @GetMapping(path = "/brands/{id}")
    public ResponseEntity<?> findBrandById (@PathVariable("id") String id){
        return brandService.findBrandById(id);
    }

    @PostMapping(path = "/admin/brands", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addBrand (@RequestParam(value = "name") String name,
                                          @RequestParam(value = "file",required = false) MultipartFile file){
        if (name == null || name.isBlank()) throw new AppException(HttpStatus.BAD_REQUEST.value(), "Name is required");
        return brandService.addBrand(name, file);
    }

    @PostMapping(path = "/admin/brands/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBrand (@PathVariable("id") String id,
                                          @RequestParam("name") String name,
                                          @RequestParam(value = "file",required = false) MultipartFile file) {
        if (name == null || name.isBlank()) throw new AppException(HttpStatus.BAD_REQUEST.value(), "Name is required");
        return brandService.updateBrand(id, name, file);
    }

    @DeleteMapping(path = "/admin/brands/{id}")
    public ResponseEntity<?> deleteBrand (@PathVariable("id") String id){
        return brandService.deactivatedBrand(id);
    }
}
