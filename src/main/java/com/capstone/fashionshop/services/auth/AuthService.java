package com.capstone.fashionshop.services.auth;

import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.mapper.UserMapper;
import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.request.LoginReq;
import com.capstone.fashionshop.payload.request.RegisterReq;
import com.capstone.fashionshop.payload.response.LoginRes;
import com.capstone.fashionshop.repository.UserRepository;
import com.capstone.fashionshop.security.jwt.JwtUtils;
import com.capstone.fashionshop.security.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    @Override
    public ResponseEntity<?> login(LoginReq req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            String access_token = jwtUtils.generateTokenFromUserId(user.getUser());
            LoginRes res = userMapper.toLoginRes(user.getUser());
            res.setAccessToken(access_token);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Log in successfully ", res)
            );

        } catch (BadCredentialsException ex) {
            ex.printStackTrace();
            throw new BadCredentialsException(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> register(RegisterReq req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new AppException(HttpStatus.CONFLICT.value(), "Email already exists");
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        User user = userMapper.toUser(req);
        if (user != null) {
            try {
                userRepository.insert(user);
            } catch (Exception e){
                throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject(true, "Register successfully ", "")
        );
    }
}
