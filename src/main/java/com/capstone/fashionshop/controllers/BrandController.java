package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.config.Constants;
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

    @GetMapping(path = "/admin/manage/brands")
    public ResponseEntity<?> findAll (@RequestParam(value = "state", defaultValue = "") String state){
        return brandService.findAll(state);
    }

    @GetMapping(path = "/brands/{id}")
    public ResponseEntity<?> findBrandById (@PathVariable("id") String id){
        return brandService.findBrandById(id);
    }

    @PostMapping(path = "/admin/manage/brands", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addBrand (@RequestParam(value = "name") String name,
                                          @RequestParam(value = "file",required = false) MultipartFile file){
        if (name == null || name.isBlank()) throw new AppException(HttpStatus.BAD_REQUEST.value(), "Name is required");
        return brandService.addBrand(name, file);
    }

    @PostMapping(path = "/admin/manage/brands/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateBrand (@PathVariable("id") String id,
                                          @RequestParam("name") String name,
                                          @RequestParam("state") String state,
                                          @RequestParam(value = "file",required = false) MultipartFile file) {
        if (name == null || name.isBlank()) throw new AppException(HttpStatus.BAD_REQUEST.value(), "Name is required");
        if (state == null || state.isBlank() || (!state.equalsIgnoreCase(Constants.ENABLE) &&
                !state.equalsIgnoreCase(Constants.DISABLE)))
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "State is invalid");
        return brandService.updateBrand(id, name, state, file);
    }

    @DeleteMapping(path = "/admin/manage/brands/{id}")
    public ResponseEntity<?> deleteBrand (@PathVariable("id") String id){
        return brandService.deactivatedBrand(id);
    }
}
