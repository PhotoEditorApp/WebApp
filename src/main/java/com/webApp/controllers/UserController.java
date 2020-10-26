package com.webApp.controllers;

import com.webApp.domain.User;
import com.webApp.json.ActionMessage;
import com.webApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/signup")
    public void signUp(@RequestBody User user) {
        userService.userSignUp(user);
    }

    @GetMapping("/users/activate/{code}")
    public ActionMessage activate(@PathVariable String code) {
        if (userService.activateUser(code))
            return new ActionMessage("User successfully activated");
        else
            return new ActionMessage("Activation is not successful");
    }

//    @GetMapping("/login")
//    public ActionMessage restrictGetToLogin(){
//        return new ActionMessage("GET request to /login is forbidden");
//    }
}
