package com.capstone.fashionshop.services.user;

import com.capstone.fashionshop.payload.request.ChangePasswordReq;
import com.capstone.fashionshop.payload.request.UserReq;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    ResponseEntity<?> findAll(Pageable pageable);
    ResponseEntity<?> findUserById(String id);
    ResponseEntity<?> updateUser(String id, UserReq userReq);
    ResponseEntity<?> updatePassword(String id, ChangePasswordReq req);
    ResponseEntity<?> updateUserAvatar(String id, MultipartFile file);
    ResponseEntity<?> deactivatedUser(String id);

}
