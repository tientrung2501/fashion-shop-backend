package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.services.shipping.AddressAPIService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/address")
public class ShippingController {
    private final AddressAPIService addressAPIService;

    @GetMapping(path = "/province")
    public ResponseEntity<?> getProvince () throws IOException {
        return addressAPIService.getData("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province");
    }
}
