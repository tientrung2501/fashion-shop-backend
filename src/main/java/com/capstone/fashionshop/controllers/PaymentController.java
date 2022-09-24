package com.capstone.fashionshop.controllers;


import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.services.payment.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
@RequestMapping("/api/checkout")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping(path = "/{paymentType}/{orderId}")
    public ResponseEntity<?> checkout (@PathVariable("paymentType") String paymentType,
                                       @PathVariable("orderId") String orderId,
                                       HttpServletRequest request) {
        return paymentService.createPayment(request, orderId, paymentType);
    }

    @GetMapping("/{paymentType}/success")
    public ResponseEntity<?> successPay(@RequestParam(value = "paymentId", required = false) String paymentId,
                           @RequestParam(value = "PayerID", required = false) String payerId,
                           @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
                           @RequestParam(value = "vnp_OrderInfo", required = false) String id,
                           @PathVariable("paymentType") String paymentType,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        switch (paymentType) {
            case Constants.PAYMENT_PAYPAL:
                return paymentService.executePayment(paymentId,payerId,null,null, request, response);
            case Constants.PAYMENT_VNPAY:
                return paymentService.executePayment(null, null, responseCode,id, request, response);
            default:
                return paymentService.executePayment(paymentId, null,null,null, request, response);
        }
    }

    @GetMapping("/{paymentType}/cancel")
    public ResponseEntity<?> cancelPay(@RequestParam(value = "paymentId", required = false) String paymentId,
                           @RequestParam(value = "token", required = false) String token,
                           @PathVariable("paymentType") String paymentType,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        if (Constants.PAYMENT_PAYPAL.equals(paymentType)) {
            return paymentService.cancelPayment(token, null, request, response);
        } else {
            return paymentService.cancelPayment(paymentId, null, request, response);
        }
    }
}
