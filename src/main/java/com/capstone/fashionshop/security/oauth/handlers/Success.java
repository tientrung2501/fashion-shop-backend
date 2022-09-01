package com.capstone.fashionshop.security.oauth.handlers;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.mapper.UserMapper;
import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.models.enums.EGender;
import com.capstone.fashionshop.models.enums.EProvider;
import com.capstone.fashionshop.payload.ResponseObject;
import com.capstone.fashionshop.payload.response.LoginRes;
import com.capstone.fashionshop.repository.UserRepository;
import com.capstone.fashionshop.security.jwt.JwtUtils;
import com.capstone.fashionshop.security.oauth.CustomOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class Success extends SavedRequestAwareAuthenticationSuccessHandler {
    private UserRepository userRepository;
    private JwtUtils jwtUtil;
    private UserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        EProvider provider = EProvider.valueOf(oauth2User.getOauth2ClientName().toUpperCase());

        Optional<User> user = userRepository.findUserByEmailAndState(oauth2User.getEmail(), Constants.USER_STATE_ACTIVATED);
        ResponseEntity<ResponseObject> res;
        if (user.isEmpty()) {
            res = processAddUser(oauth2User, provider);
        } else {
            try {
                if (EnumUtils.isValidEnum(EProvider.class, user.get().getProvider().name()) &&
                        !user.get().getProvider().equals(EProvider.LOCAL) &&
                        provider.equals(user.get().getProvider())) {
                    String accessToken = jwtUtil.generateTokenFromUserId(user.get());
                    LoginRes userLoginRes = userMapper.toLoginRes(user.get());
                    userLoginRes.setAccessToken(accessToken);
                    res = ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject(true, "Login successful with google", userLoginRes)
                    );
                } else res = ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new ResponseObject(false, "Your email already have an account with other method", "")
                );
            } catch (NullPointerException e) {
                res = ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new ResponseObject(false, "Your email already have an account", e.getMessage())
                );
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(res);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    public ResponseEntity<ResponseObject> processAddUser(CustomOAuth2User oAuth2User, EProvider provider) {
        User newUser = new User(oAuth2User.getName(), oAuth2User.getEmail(), "",
                "", "", Constants.ROLE_USER, oAuth2User.getProfilePicture(), EGender.OTHER,
                Constants.USER_STATE_ACTIVATED, provider);
        userRepository.save(newUser);
        String accessToken = jwtUtil.generateTokenFromUserId(newUser);
        LoginRes res = userMapper.toLoginRes(newUser);
        res.setAccessToken(accessToken);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject(true, "Login and create user successful with google", res)
        );
    }
}
