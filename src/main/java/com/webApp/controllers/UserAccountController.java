package com.webapp.service;

import com.webapp.domain.User;
import com.webapp.json.ActionMessage;
import com.webapp.service.UserService;
import UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserAccountController {
    @Autowired
    private UserAccountService userService;

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