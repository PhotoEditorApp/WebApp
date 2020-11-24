package com.webapp.controllers;

import com.webapp.domain.UserAccount;
import com.webapp.enums.AccessType;
import com.webapp.json.ActionMessage;
import com.webapp.json.SpaceResponse;
import com.webapp.json.UserAccountResponseMessage;
import com.webapp.service.SpaceService;
import com.webapp.service.TagService;
import com.webapp.service.UserAccountService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserAccountService userAccountService;
    private final SpaceService spaceService;
    private final TagService tagService;

    public UserController(UserAccountService userAccountService, SpaceService spaceService,TagService tagService) {
        this.userAccountService = userAccountService;
        this.spaceService = spaceService;
        this.tagService = tagService;
    }

    // sign up user by email and password
    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestParam String email, @RequestParam String password) {
        try {;
            userAccountService.userSignUp(email, password);
            return new ResponseEntity<>("user successfully signed up but not activated", HttpStatus.OK);
        }
        catch(Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/activate/{code}")
    public ActionMessage activate(@PathVariable String code) {
        if (userAccountService.activateUser(code))
            return new ActionMessage("User successfully activated");
        else
            return new ActionMessage("Activation is not successful");
    }

    // get user by email
    @GetMapping
    public HttpEntity<? extends Serializable> findUserByEmail(@RequestParam(name="email") String email){
        Optional<UserAccount> user = userAccountService.findByEmail(email);
        if (user.isPresent()){
           return new ResponseEntity<>(new UserAccountResponseMessage(user.get()), HttpStatus.OK);
        }
        else{
           return new ResponseEntity<>("cannot find user by email", HttpStatus.NOT_FOUND);
        }
    }

    // get all available spaces of user by params
    @GetMapping("/{user_id}/space")
    public HttpEntity<? extends Serializable> getSpacesByUserId(@PathVariable Long user_id,
                                                                @RequestParam(required = false) Optional<AccessType> type){
        try{
            // form spaces, which convert to json response
            ArrayList<SpaceResponse> spaces = new ArrayList<>();
            spaceService.getSpacesByUserId(user_id, type).forEach(space ->
                spaces.add(new SpaceResponse(space))
            );


            return new ResponseEntity<>(spaces, HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // get all user's tag
    @GetMapping("/{id}/tag")
    public ResponseEntity<?> getUserTags(@PathVariable Long id){
        try{
            return new ResponseEntity<>(tagService.getUserTags(id), HttpStatus.OK);
        }
        catch(Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // get user by id
    @GetMapping("{id}")
    public HttpEntity<? extends Serializable> getUserAccountById(@PathVariable Long id){
        try {
            UserAccount userAccount = userAccountService.findById(id)
                    .orElseThrow(() -> new Exception("No such account: id = " + id.toString()));
            return new ResponseEntity<>(userAccount, HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
