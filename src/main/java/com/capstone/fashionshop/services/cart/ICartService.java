package com.capstone.fashionshop.services.cart;

import com.capstone.fashionshop.payload.request.CartReq;
import org.springframework.http.ResponseEntity;

public interface ICartService {
    ResponseEntity<?> getProductFromCart(String userId);
    ResponseEntity<?> addAndUpdateProductToCart(String userId, CartReq req);
    ResponseEntity<?> deleteProductFromCart(String userId, String orderItemId);
}
