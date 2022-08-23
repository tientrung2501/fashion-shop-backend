package com.capstone.fashionshop.services.impl;

import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.repository.UserRepository;
import com.capstone.fashionshop.services.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private UserRepository userRepository;
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
