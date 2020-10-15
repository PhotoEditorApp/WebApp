package com.webApp.testPart.controllers;

import com.webApp.testPart.domain.User;
import com.webApp.testPart.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    public UserController(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.userRepo = userRepo;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }

    @PostMapping("/signup")
    public void signUp(@RequestBody User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}
