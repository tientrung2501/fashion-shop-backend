package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.payload.request.ShippingReq;
import com.capstone.fashionshop.services.shipping.AddressAPIService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/shipping")
public class ShippingController {
    private final AddressAPIService addressAPIService;

    @PostMapping(path = "/province", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProvince () {
        return addressAPIService.getProvince();
    }

    @PostMapping(path = "/district", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDistrict (@RequestBody ShippingReq req) {
        return addressAPIService.getDistrict(req.getProvince_id());
    }

    @PostMapping(path = "/ward", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWard (@RequestBody ShippingReq req) {
        return addressAPIService.getWard(req.getDistrict_id());
    }
}
