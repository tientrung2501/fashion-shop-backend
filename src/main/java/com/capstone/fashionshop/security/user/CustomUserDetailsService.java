package com.capstone.fashionshop.security.user;

import com.capstone.fashionshop.config.Constants;
import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmailAndState(email, Constants.USER_STATE_ACTIVATED);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found with user id: " + email);
        }
        return new CustomUserDetails(user.get());
    }
}
