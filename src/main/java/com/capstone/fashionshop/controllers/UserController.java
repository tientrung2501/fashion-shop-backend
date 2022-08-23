package com.capstone.fashionshop.controllers;

import com.capstone.fashionshop.models.entities.User;
import com.capstone.fashionshop.services.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private IUserService userService;

    @GetMapping(path = "/user/all")
    public List<User> findAll (){
        return userService.findAll();
    }
}
