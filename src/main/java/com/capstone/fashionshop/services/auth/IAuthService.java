package com.capstone.fashionshop.services.auth;

import com.capstone.fashionshop.payload.request.LoginReq;
import com.capstone.fashionshop.payload.request.RegisterReq;
import com.capstone.fashionshop.payload.request.VerifyOTPReq;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<?> login(LoginReq req);
    ResponseEntity<?> register(RegisterReq req);
    ResponseEntity<?> reset(String email);
    ResponseEntity<?> verifyOTP(VerifyOTPReq req);
}
