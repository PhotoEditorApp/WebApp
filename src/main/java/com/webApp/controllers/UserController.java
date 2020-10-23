package com.webApp.controllers;

import com.webApp.domain.User;
import com.webApp.json.ActivateMessage;
import com.webApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public void signUp(@RequestBody User user) {
        userService.userSignUp(user);
    }

    @GetMapping("/activate/{code}")
    public ActivateMessage activate(@PathVariable String code) {
        if (userService.activateUser(code))
            return new ActivateMessage("User successfully activated");
        else
            return new ActivateMessage("Activation is not successful");
    }
}
