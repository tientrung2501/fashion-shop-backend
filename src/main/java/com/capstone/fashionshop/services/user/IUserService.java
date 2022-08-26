package com.capstone.fashionshop.services.user;

import com.capstone.fashionshop.models.entities.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();
}
