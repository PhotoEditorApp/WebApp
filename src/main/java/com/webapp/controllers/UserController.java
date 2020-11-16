package com.webapp.controllers;

import com.webapp.domain.UserAccount;
import com.webapp.json.ActionMessage;
import com.webapp.json.UserAccountResponseMessage;
import com.webapp.service.UserAccountService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserAccountService userAccountService;

    public UserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    // sign up user by email and password
    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestParam String email, @RequestParam String password) {
        try {;
            userAccountService.userSignUp(email, password);
            return new ResponseEntity<>("user successfully signed up", HttpStatus.OK);
        }
        catch(Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("activate/{code}")
    public ActionMessage activate(@PathVariable String code) {
        if (userAccountService.activateUser(code))
            return new ActionMessage("User successfully activated");
        else
            return new ActionMessage("Activation is not successful");
    }

    // get user by email
    @GetMapping
    public HttpEntity<? extends Serializable> findUserByEmail(@RequestParam String email){
        Optional<UserAccount> user = userAccountService.findByEmail(email);
        if (user.isPresent()){
           return new ResponseEntity<>(new UserAccountResponseMessage(user.get()), HttpStatus.OK);
        }
        else{
           return new ResponseEntity<>("cannot find user by email", HttpStatus.NOT_FOUND);
        }
    }

    // get all users, which are connected with space
//    @GetMapping
//    public HttpEntity<? extends Serializable> getUsersBySpace(@RequestParam Long space_id){
//        try {
//            return new ResponseEntity<>(userAccountService.getUsersBySpaceId(space_id), HttpStatus.OK);
//        }
//        catch (Exception exception){
//            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
//        }
//    }

    // get user by id
    @GetMapping("{id}")
    public HttpEntity<? extends Serializable> getUserAccountById(@PathVariable Long id){
        try {
            return new ResponseEntity<>(userAccountService.findById(id).get(), HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
