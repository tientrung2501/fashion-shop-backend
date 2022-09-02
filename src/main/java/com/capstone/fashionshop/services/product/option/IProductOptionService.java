package com.capstone.fashionshop.services.product.option;

import com.capstone.fashionshop.payload.request.ProductOptionReq;
import org.springframework.http.ResponseEntity;

public interface IProductOptionService {
    ResponseEntity<?> addOption(String productId ,ProductOptionReq req);
    ResponseEntity<?> findOptionById(String id);
    ResponseEntity<?> findOptionByProductId(String id);
    ResponseEntity<?> updateOption(String id, ProductOptionReq req);
}
