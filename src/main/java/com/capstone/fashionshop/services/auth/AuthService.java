package com.capstone.fashionshop.services.auth;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.mapper.UserMapper;
import com.capstone.fashionshop.models.entities.Token;
import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.request.LoginReq;
import com.capstone.fashionshop.payload.request.RegisterReq;
import com.capstone.fashionshop.payload.request.VerifyOTPReq;
import com.capstone.fashionshop.payload.response.LoginRes;
import com.capstone.fashionshop.repository.UserRepository;
import com.capstone.fashionshop.security.jwt.JwtUtils;
import com.capstone.fashionshop.security.user.CustomUserDetails;
import com.capstone.fashionshop.services.mail.EMailType;
import com.capstone.fashionshop.services.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final MailService mailService;

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
//            ex.printStackTrace();
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
                sendVerifyMail(user);
//                userRepository.insert(user);
            } catch (Exception e){
                throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject(true, "Register successfully ", "")
        );
    }

    @Override
    public ResponseEntity<?> reset(String email) {
        Optional<User> user = userRepository.findUserByEmailAndState(email, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            try {
                sendVerifyMail(user.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(true, "Send email reset password success", email));
            } catch (Exception e) {
                throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), "Failed to send reset email");
            }
        } throw new  NotFoundException("Can not found user with email " + email + " is activated");
    }

    @SneakyThrows
    public void sendVerifyMail(User user) {
        String token = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        user.setToken(new Token(token, LocalDateTime.now().plusMinutes(5)));
        userRepository.save(user);
        mailService.sendEmail(user.getEmail(), model, EMailType.AUTH);
    }

    @Override
    public ResponseEntity<?> verifyOTP(VerifyOTPReq req) {
        switch (req.getType().toLowerCase()) {
            case "register": return verifyRegister(req.getEmail(), req.getOtp());
            case "reset": return verifyReset(req.getEmail(), req.getOtp());
            default: throw new NotFoundException("Can not found type of verify");
        }
    }

    private ResponseEntity<?> verifyReset(String email, String otp) {
        Optional<User> user = userRepository.findUserByEmailAndState(email, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            Map<String, Object> res = new HashMap<>();
            boolean verify = false;
            if (user.get().getToken().getOtp().equals(otp) &&
                    LocalDateTime.now().isBefore(user.get().getToken().getExp())) {
                res.put("id", user.get().getId());
                res.put("token", jwtUtils.generateTokenFromUserId(user.get()));
                verify = true;
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(verify, "OTP with email: " + email + " is " + verify, res));
        } throw new  NotFoundException("Can not found user with email " + email + " is activated");
    }

    private ResponseEntity<?> verifyRegister(String email, String otp) {
        Optional<User> user = userRepository.findUserByEmailAndState(email, Constants.USER_STATE_UNVERIFIED);
        if (user.isPresent()) {
            boolean verify = false;
            if (user.get().getToken().getOtp().equals(otp) &&
                    LocalDateTime.now().isBefore(user.get().getToken().getExp())) {
                user.get().setState(Constants.USER_STATE_ACTIVATED);
                userRepository.save(user.get());
                verify = true;
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(verify, "OTP with email: " + email + " is " + verify, ""));
        } throw new  NotFoundException("Can not found user with email " + email);
    }
}
