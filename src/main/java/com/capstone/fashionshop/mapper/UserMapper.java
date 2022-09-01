package com.capstone.fashionshop.mapper;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.exception.AppException;
import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.models.enums.EGender;
import com.capstone.fashionshop.models.enums.EProvider;
import com.capstone.fashionshop.payload.request.RegisterReq;
import com.capstone.fashionshop.payload.response.LoginRes;
import com.capstone.fashionshop.payload.response.UserRes;
import com.capstone.fashionshop.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserMapper {
    public LoginRes toLoginRes(User user) {
        LoginRes loginRes = new LoginRes();
        if (user != null) {
            loginRes.setId(user.getId());
            loginRes.setName(user.getName());
            loginRes.setEmail(user.getEmail());
            loginRes.setAvatar(user.getAvatar());
            loginRes.setRole(user.getRole());
            loginRes.setGender(user.getGender());
        }
        return loginRes;
    }

    public UserRes toUserRes(User user) {
        UserRes userRes = new UserRes();
        if (user != null) {
            userRes.setId(user.getId());
            userRes.setName(user.getName());
            userRes.setEmail(user.getEmail());
            userRes.setAvatar(user.getAvatar());
            userRes.setRole(user.getRole());
            userRes.setState(user.getState());
            userRes.setGender(user.getGender());
            userRes.setPhone(user.getPhone());
            userRes.setAddress(user.getAddress());
        }
        return userRes;
    }

    public User toUser(RegisterReq req) {
        if (req != null) {
            EGender gender;
            if (!StringUtils.isPhoneNumberFormat(req.getPhone()))
                throw new AppException(400, "Phone number is invalid!");
            try {
                gender = EGender.valueOf(req.getGender().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new AppException(400, "Gender is invalid!");
            }
            return new User(req.getName(), req.getEmail(), req.getPassword(), req.getPhone(),
                    req.getAddress(), Constants.ROLE_USER, null,
                    gender, Constants.USER_STATE_ACTIVATED, EProvider.LOCAL);
        }
        return null;
    }
}
