package com.webApp.testPart.controllers;

import com.webApp.testPart.domain.User;
import com.webApp.testPart.json.ActivateMessage;
import com.webApp.testPart.service.UserService;
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
        int a = 1;
        if (userService.activateUser(code))
            return new ActivateMessage("User successfully activated");
        else
            return new ActivateMessage("Activation is not successful");
    }
}
