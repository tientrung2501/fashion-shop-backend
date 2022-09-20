package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.payload.request.LoginReq;
import com.capstone.fashionshop.payload.request.RegisterReq;
import com.capstone.fashionshop.payload.request.VerifyOTPReq;
import com.capstone.fashionshop.services.auth.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginReq loginReq) {
        return authService.login(loginReq);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterReq registerReq) {
        return authService.register(registerReq);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody VerifyOTPReq req) {
        if (!req.getEmail().isBlank()) return authService.reset(req.getEmail());
        throw new AppException(HttpStatus.BAD_REQUEST.value(), "Email is required");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@Valid @RequestBody VerifyOTPReq req) {
        return authService.verifyOTP(req);
    }

}
