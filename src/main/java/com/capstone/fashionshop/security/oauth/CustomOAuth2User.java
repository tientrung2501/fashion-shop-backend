package com.capstone.fashionshop.security.oauth;

import com.capstone.fashionshop.models.enums.EProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final String oauth2ClientName;
    private final OAuth2User oauth2User;

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

    public String getProfilePicture() {
        if (EProvider.valueOf(oauth2ClientName.toUpperCase(Locale.ROOT)) == EProvider.FACEBOOK) {
            if(oauth2User.getAttributes().containsKey("picture")) {
                Map<String, Object> pictureObj = (Map<String, Object>) oauth2User.getAttributes().get("picture");
                if(pictureObj.containsKey("data")) {
                    Map<String, Object>  dataObj = (Map<String, Object>) pictureObj.get("data");
                    if(dataObj.containsKey("url")) {
                        return  (String) dataObj.get("url");
                    }
                }
            }
            return "";
        } else return oauth2User.getAttribute("picture");
    }

    public String getOauth2ClientName() {
        return this.oauth2ClientName;
    }
}
