package com.webapp.controllers;

import com.webapp.domain.UserAccount;
import com.webapp.json.ActionMessage;
import com.webapp.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {
    private final UserAccountService userAccountService;

    public UserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/users/signup")
    public void signUp(@RequestBody UserAccount userAccount) {
        userAccountService.userSignUp(userAccount);
    }

    @GetMapping("/users/activate/{code}")
    public ActionMessage activate(@PathVariable String code) {
        if (userAccountService.activateUser(code))
            return new ActionMessage("User successfully activated");
        else
            return new ActionMessage("Activation is not successful");
    }


    @GetMapping("/user/{email}")
    public UserAccount findUserByEmail(@PathVariable String email){
        // Поиск пользователя по почтe
        return userAccountService.findByEmail(email);
    }

}
