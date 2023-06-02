package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.payload.request.ShippingReq;
import com.capstone.fashionshop.services.shipping.ShippingAPIService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/shipping")
public class ShippingController {
    private final ShippingAPIService shippingAPIService;

    @PostMapping(path = "/province", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProvince () {
        return shippingAPIService.getProvince();
    }

    @PostMapping(path = "/district", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDistrict (@RequestBody ShippingReq req) {
        return shippingAPIService.getDistrict(req.getProvince_id());
    }

    @PostMapping(path = "/ward", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWard (@RequestBody ShippingReq req) {
        return shippingAPIService.getWard(req.getDistrict_id());
    }

    @PostMapping(path = "/fee", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calculateFee (@RequestBody ShippingReq req) {
        return shippingAPIService.calculateFee(req);
    }

    @PostMapping(path = "/expectedTime", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calculateExpectedDeliveryTime (@RequestBody ShippingReq req) {
        return shippingAPIService.calculateExpectedDeliveryTime(req);
    }

    @PostMapping(path = "/service", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getService (@RequestBody ShippingReq req) {
        return shippingAPIService.getService(req);
    }

    @PostMapping(path = "/detail/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDetail (@PathVariable String orderId) {
        return shippingAPIService.getDetail(orderId);
    }
}
