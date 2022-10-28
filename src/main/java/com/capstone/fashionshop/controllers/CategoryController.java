package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.payload.request.CategoryReq;
import com.capstone.fashionshop.services.category.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CategoryController {
    private final ICategoryService categoryService;

    @GetMapping(path = "/categories")
    public ResponseEntity<?> findRoot (@RequestParam(value = "root", defaultValue = "true") Boolean root){
        return categoryService.findRoot(root);
    }

    @GetMapping(path = "/categories/{id}")
    public ResponseEntity<?> findCategoryById (@PathVariable("id") String id){
        return categoryService.findCategoryById(id);
    }

    @GetMapping(path = "/admin/manage/categories")
    public ResponseEntity<?> findAll (){
        return categoryService.findAll();
    }

    @PostMapping(path = "/admin/manage/categories", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCategory (@ModelAttribute @Valid CategoryReq req){
        return categoryService.addCategory(req);
    }

    @PutMapping(path = "/admin/manage/categories/{id}")
    public ResponseEntity<?> updateCategory (@PathVariable("id") String id,
                                             @RequestBody @Valid CategoryReq req){
        return categoryService.updateCategory(id, req);
    }

    @PostMapping(path = "/admin/manage/categories/image/{id}")
    public ResponseEntity<?> updateCategory (@PathVariable("id") String id,
                                             @RequestParam(value = "file") MultipartFile file){
        return categoryService.updateCategoryImage(id, file);
    }

    @DeleteMapping(path = "/admin/manage/categories/{id}")
    public ResponseEntity<?> deleteCategory (@PathVariable("id") String id){
        return categoryService.deactivatedCategory(id);
    }
}
