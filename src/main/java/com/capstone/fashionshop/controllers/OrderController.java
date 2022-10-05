package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.services.order.IOrderService;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final IOrderService orderService;

    @GetMapping(path = "/admin/manage/orders")
    public ResponseEntity<?> findAll (@PageableDefault(size = 5) @ParameterObject Pageable pageable){
        return orderService.findAll(pageable);
    }
}
