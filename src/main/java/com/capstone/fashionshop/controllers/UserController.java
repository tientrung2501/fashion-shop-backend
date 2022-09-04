package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.payload.request.UserReq;
import com.capstone.fashionshop.security.jwt.JwtUtils;
import com.capstone.fashionshop.services.user.IUserService;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final IUserService userService;
    private final JwtUtils jwtUtils;

    @GetMapping(path = "/admin/users")
    public ResponseEntity<?> findAll (@PageableDefault(size = 5) @ParameterObject Pageable pageable){
        return userService.findAll(pageable);
    }

    @DeleteMapping(path = "/admin/users/{userId}")
    public ResponseEntity<?> deactivatedUser (@PathVariable("userId") String userId){
        return userService.deactivatedUser(userId);
    }

    @PutMapping(path = "/users/{userId}")
    public ResponseEntity<?> updateUser (@Valid @RequestBody UserReq req,
                                         @PathVariable("userId") String userId,
                                         HttpServletRequest request){
        User user = jwtUtils.getUserFromJWT(jwtUtils.getJwtFromHeader(request));
        if (user.getId().equals(userId) || !user.getId().isBlank())
            return userService.updateUser(userId, req);
        throw new AppException(HttpStatus.FORBIDDEN.value(), "You don't have permission! Token is invalid");
    }

    @PostMapping(path = "/users/avatar/{userId}")
    public ResponseEntity<?> updateUser (@PathVariable("userId") String userId,
                                         HttpServletRequest request,
                                         @RequestParam MultipartFile file){
        User user = jwtUtils.getUserFromJWT(jwtUtils.getJwtFromHeader(request));
        if (user.getId().equals(userId) || !user.getId().isBlank())
            return userService.updateUserAvatar(userId, file);
        throw new AppException(HttpStatus.FORBIDDEN.value(), "You don't have permission! Token is invalid");
    }

    @GetMapping(path = "/users/{userId}")
    public ResponseEntity<?> findUserById (@PathVariable("userId") String userId){
        return userService.findUserById(userId);
    }
}
