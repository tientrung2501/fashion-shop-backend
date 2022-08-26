package com.capstone.fashionshop.mapper;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.models.enums.EGender;
import com.capstone.fashionshop.models.enums.EProvider;
import com.capstone.fashionshop.payload.request.RegisterReq;
import com.capstone.fashionshop.payload.response.LoginRes;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserMapper {
    public LoginRes toLoginRes(User user) {
        LoginRes loginRes = new LoginRes();
        if (user != null) {
            if (user.getName() != null && !user.getName().isBlank())
                loginRes.setName(user.getName());
            if (user.getEmail() != null && !user.getEmail().isBlank())
                loginRes.setEmail(user.getEmail());
            if (user.getAvatar() != null && !user.getAvatar().isBlank())
                loginRes.setAvatar(user.getAvatar());
            if (user.getRole() != null && !user.getRole().isBlank())
                loginRes.setRole(user.getRole());
            if (user.getGender() != null)
                loginRes.setGender(user.getGender());
        }
        return loginRes;
    }

    public User toUser(RegisterReq req) {
        if (req != null) {
            return new User(req.getName(), req.getEmail(), req.getPassword(), req.getPhone(),
                    req.getAddress(), Constants.ROLE_USER, "",
                    EGender.valueOf(req.getGender().toUpperCase(Locale.ROOT)),
                    Constants.USER_STATE_ACTIVATED, EProvider.LOCAL);
        }
        return null;
    }
}
