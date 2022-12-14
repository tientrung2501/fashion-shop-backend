package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.payload.request.ProductOptionReq;
import com.capstone.fashionshop.services.product.option.IProductOptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ProductOptionController {
    private IProductOptionService productOptionService;

    @PostMapping(value = "/manage/products/option/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addOption(@PathVariable("productId") String id,
                                        @Valid @ModelAttribute ProductOptionReq req) {
        return productOptionService.addOption(id, req);
    }

    @PutMapping(value = "/manage/products/option/{id}")
    public ResponseEntity<?> updateOptionVariant(@PathVariable("id") String id,
                                                 @RequestParam("variantColor") String variantColor,
                                                 @Valid @RequestBody ProductOptionReq req) {
        variantColor = "#"+variantColor;
        return productOptionService.updateOptionVariant(id, variantColor, req);
    }

    @GetMapping("/products/option")
    public ResponseEntity<?> findOptionByProductId(@RequestParam("productId") String id) {
        return productOptionService.findOptionByProductId(id);
    }

    @GetMapping("/products/option/{id}")
    public ResponseEntity<?> getOptionById(@PathVariable("id") String id) {
        return productOptionService.findOptionById(id);
    }
}
