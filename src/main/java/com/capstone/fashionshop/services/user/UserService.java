package com.capstone.fashionshop.services.user;

import com.capstone.fashionshop.config.CloudinaryConfig;
import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.exception.NotFoundException;
import com.capstone.fashionshop.mapper.OrderMapper;
import com.capstone.fashionshop.mapper.UserMapper;
import com.capstone.fashionshop.models.entities.order.Order;
import com.capstone.fashionshop.models.entities.product.Product;
import com.capstone.fashionshop.models.entities.user.User;
import com.capstone.fashionshop.models.enums.EGender;
import com.capstone.fashionshop.models.enums.EProvider;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.request.ChangePasswordReq;
import com.capstone.fashionshop.payload.request.RegisterReq;
import com.capstone.fashionshop.payload.request.UserReq;
import com.capstone.fashionshop.payload.response.OrderRes;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CloudinaryConfig cloudinary;
    private final PasswordEncoder passwordEncoder;
    private final OrderMapper orderMapper;
    @Override
    public ResponseEntity<?> findAll(String state, Pageable pageable) {
        Page<User> users;
        if (state.equalsIgnoreCase(Constants.USER_STATE_ACTIVATED) ||
                state.equalsIgnoreCase(Constants.USER_STATE_DEACTIVATED) ||
                state.equalsIgnoreCase(Constants.USER_STATE_UNVERIFIED))
            users = userRepository.findAllByState(state.toLowerCase(), pageable);
        else users = userRepository.findAll(pageable);
        List<UserRes> userResList = users.stream().map(userMapper::toUserRes).collect(Collectors.toList());
        Map<String, Object> resp = new HashMap<>();
        resp.put("list", userResList);
        resp.put("totalQuantity", users.getTotalElements());
        resp.put("totalPage", users.getTotalPages());
        if (userResList.size() > 0)
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get all user success", resp));
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
    public ResponseEntity<?> getUserOrderHistory(String id) {
        Optional<User> user = userRepository.findUserByIdAndState(id, Constants.USER_STATE_ACTIVATED);
        if (user.isPresent()) {
            Comparator<Order> comparatorDesc = Comparator.comparing(Order::getCreatedDate).reversed();
            List<Order> orders = user.get().getOrders();
            orders.sort(comparatorDesc);
            List<OrderRes> resList = orders.stream().map(orderMapper::toOrderRes).collect(Collectors.toList());
            if (resList.isEmpty()) throw new NotFoundException("Can not found any orders");
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject(true, "Get order history of user success", resList));
        }
        throw new NotFoundException("Can not found user with id " + id );
    }

    @Override
    @Transactional
    public ResponseEntity<?> addUser(RegisterReq req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new AppException(HttpStatus.CONFLICT.value(), "Email already exists");
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        User user = userMapper.toUser(req);
        if (user != null) {
            if (req.getRole().toUpperCase(Locale.ROOT).equals(Constants.ROLE_STAFF) ||
                    req.getRole().toUpperCase(Locale.ROOT).equals(Constants.ROLE_USER))
                user.setRole(req.getRole().toUpperCase());
            else throw new NotFoundException("Can not found role: "+ req.getRole());
            user.setState(Constants.USER_STATE_ACTIVATED);
            try {
                userRepository.insert(user);
            } catch (Exception e){
                throw new AppException(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject(true, "Add user successfully ", "")
        );
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateUser(String id, UserReq userReq) {
        Optional<User> user;
        if (userReq.getState() == null)
            user = userRepository.findUserByIdAndState(id, Constants.USER_STATE_ACTIVATED);
        else user = userRepository.findById(id);
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
            if (src.getProvince() > 0)
                des.setProvince(src.getProvince());
            if (src.getDistrict() > 0)
                des.setDistrict(src.getDistrict());
            if (src.getWard() > 0)
                des.setWard(src.getWard());
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
            if (src.getState() != null && !src.getState().isEmpty())
                if (src.getState().equals(Constants.USER_STATE_ACTIVATED) ||
                    src.getState().equals(Constants.USER_STATE_DEACTIVATED))
                    des.setState(src.getState());
                else throw new AppException(HttpStatus.BAD_REQUEST.value(), "State is invalid!");
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
            if (!user.get().getProvider().equals(EProvider.LOCAL)) throw new AppException(HttpStatus.BAD_REQUEST.value(), "Your account is " +
                    user.get().getProvider() + " account");
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
        if (user.isPresent() && user.get().getToken() != null) {
            if (!user.get().getProvider().equals(EProvider.LOCAL)) throw new AppException(HttpStatus.BAD_REQUEST.value(), "Your account is " +
                    user.get().getProvider() + " account");
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
