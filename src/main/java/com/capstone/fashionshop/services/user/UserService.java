package com.capstone.fashionshop.services.user;

import com.capstone.fashionshop.config.CloudinaryConfig;
import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.mapper.UserMapper;
import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.models.enums.EGender;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.request.ChangePasswordReq;
import com.capstone.fashionshop.payload.request.UserReq;
import com.capstone.fashionshop.payload.response.UserRes;
import com.capstone.fashionshop.repository.UserRepository;
import com.capstone.fashionshop.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CloudinaryConfig cloudinary;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<UserRes> userResList = users.stream().map(userMapper::toUserRes).collect(Collectors.toList());
        if (userResList.size() > 0)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get all user success", userResList));
        throw new NotFoundException("Can not found any user");
    }

    @Override
    public ResponseEntity<?> findUserById(String id) {
        Optional<User> user = userRepository.findUserByIdAndState(id, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            UserRes res = userMapper.toUserRes(user.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get user success", res));
        }
        throw new NotFoundException("Can not found user with id " + id );
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateUser(String id, UserReq userReq) {
        Optional<User> user = userRepository.findUserByIdAndState(id, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            updateUserProcess(userReq, user.get());
            userRepository.save(user.get());
            UserRes res = userMapper.toUserRes(user.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Update user success", res));
        }
        throw new NotFoundException("Can not found user with id " + id );
    }

    public void updateUserProcess(UserReq src, User des) {
        if (src != null) {
            if (!src.getName().isBlank())
                des.setName(src.getName());
            if (!src.getAddress().isBlank())
                des.setAddress(src.getAddress());
            if (!src.getPhone().isBlank() && StringUtils.isPhoneNumberFormat(src.getPhone()))
                des.setPhone(src.getPhone());
            else throw new AppException(HttpStatus.BAD_REQUEST.value(), "Phone number is invalid!");
            if (!src.getGender().isBlank())
                try {
                    des.setGender(EGender.valueOf(src.getGender().toUpperCase(Locale.ROOT)));
                } catch (IllegalArgumentException e) {
                    throw new AppException(HttpStatus.BAD_REQUEST.value(), "Gender is invalid!");
                }
        }
    }

    @Override
    public ResponseEntity<?> updateUserAvatar(String id, MultipartFile file) {
        Optional<User> user = userRepository.findUserByIdAndState(id, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            if (file != null && !file.isEmpty()) {
                try {
                    String imgUrl = cloudinary.uploadImage(file, user.get().getAvatar());
                    user.get().setAvatar(imgUrl);
                    userRepository.save(user.get());
                } catch (IOException e) {
                    throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), "Error when upload image");
                }
            }
            UserRes res = userMapper.toUserRes(user.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Update user success", res));
        }
        throw new NotFoundException("Can not found user with id " + id );
    }

    @Override
    @Transactional
    public ResponseEntity<?> deactivatedUser(String id) {
        Optional<User> user = userRepository.findUserByIdAndState(id, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            user.get().setState(Constants.USER_STATE_DEACTIVATED);
            userRepository.save(user.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Delete user success", ""));
        }
        throw new NotFoundException("Can not found user with id " + id + " is activated");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updatePassword(String id, ChangePasswordReq req) {
        Optional<User> user = userRepository.findUserByIdAndState(id, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            if (passwordEncoder.matches(req.getOldPassword(), user.get().getPassword())
            && !req.getNewPassword().equals(req.getOldPassword())) {
                user.get().setPassword(passwordEncoder.encode(req.getNewPassword()));
                userRepository.save(user.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(true, "Change password success", ""));
            } else throw new AppException(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Your old password is wrong" +
                    " or same with new password");
        }
        throw new NotFoundException("Can not found user with id " + id + " is activated");
    }

    @Override
    @Transactional
    public ResponseEntity<?> updatePasswordReset(String id, ChangePasswordReq req) {
        Optional<User> user = userRepository.findUserByIdAndState(id, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            if (req.getOldPassword().equals(user.get().getToken().getOtp())) {
                user.get().setPassword(passwordEncoder.encode(req.getNewPassword()));
                user.get().setToken(null);
                userRepository.save(user.get());
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject(true, "Change password success", ""));
            } else throw new AppException(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Your otp is wrong");
        }
        throw new NotFoundException("Can not found user with id " + id + " is activated");
    }
}
