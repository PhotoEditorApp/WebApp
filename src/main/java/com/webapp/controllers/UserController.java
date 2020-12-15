package com.webapp.controllers;

import com.webapp.domain.UserAccount;
import com.webapp.enums.AccessType;
import com.webapp.json.ActionMessage;
import com.webapp.json.SpaceResponse;
import com.webapp.json.UserAccountResponseMessage;
import com.webapp.service.*;
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
    private final ImageTagService imageTagService;
    private final ImageRatingService imageRaitingService;

    public UserController(UserAccountService userAccountService, SpaceService spaceService, TagService tagService, ImageTagService imageTagService, ImageRatingService imageRaitingService) {
        this.userAccountService = userAccountService;
        this.spaceService = spaceService;
        this.tagService = tagService;
        this.imageTagService = imageTagService;
        this.imageRaitingService = imageRaitingService;
    }

    // sign up user by email and password
    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestParam String email, @RequestParam String password) {
        try {

            userAccountService.userSignUp(email, password);
            return new ResponseEntity<>("user successfully signed up but not activated", HttpStatus.OK);
        } catch (Exception exception) {
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
    public HttpEntity<? extends Serializable> findUserByEmail(@RequestParam(name = "email") String email) {
        Optional<UserAccount> user = userAccountService.findByEmail(email);
        if (user.isPresent()) {
            return new ResponseEntity<>(new UserAccountResponseMessage(user.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("cannot find user by email", HttpStatus.NOT_FOUND);
        }
    }

    // get all available spaces of user by params
    @GetMapping("/{user_id}/space")
    public HttpEntity<? extends Serializable> getSpacesByUserId(@PathVariable Long user_id,
                                                                @RequestParam(required = false) Optional<AccessType> type) {
        try {
            // form spaces, which convert to json response
            ArrayList<SpaceResponse> spaces = new ArrayList<>();
            spaceService.getSpacesByUserId(user_id, type).forEach(space ->
                    spaces.add(new SpaceResponse(space))
            );


            return new ResponseEntity<>(spaces, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // get user by id
    @GetMapping("/{user_id}")
    public HttpEntity<? extends Serializable> getUserAccountById(@PathVariable Long user_id) {
        try {
            UserAccount userAccount = userAccountService.findById(user_id)
                    .orElseThrow(() -> new Exception("No such account: id = " + user_id.toString()));
            return new ResponseEntity<>(userAccount, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // delete user space if user is CREATOR, else delete access
    @Transactional
    @DeleteMapping("/{user_id}/space/{space_id}")
    public ResponseEntity<String> deleteSpace(@PathVariable Long space_id, @PathVariable Long user_id) {
        try {
            spaceService.deleteByUserIdAndSpaceId(space_id, user_id);
            return new ResponseEntity<>("The space has been deleted", HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // get all user's tag
    @GetMapping("/{user_id}/tag")
    public ResponseEntity<?> getUserTags(@PathVariable Long user_id) {
        try {
            return new ResponseEntity<>(tagService.getUserTags(user_id), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // delete tag of user
    @Transactional
    @DeleteMapping("/{user_id}/tag/{tag_name}")
    public ResponseEntity<?> deleteTag(@PathVariable Long user_id, @PathVariable String tag_name) {
        try {
            tagService.delete(user_id, tag_name);
            return new ResponseEntity<>("tag was successfully deleted", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // add new tag
    @PutMapping("/{user_id}/tag/{tag_name}")
    public ResponseEntity<?> addTag(@PathVariable Long user_id, @PathVariable String tag_name) {
        try {
            tagService.save(tag_name, user_id);
            return new ResponseEntity<>("tag has been successfully created", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // tag image
    @PutMapping("/{user_id}/image/{image_id}/image_tag/{tag_name}")
    public ResponseEntity<?> tagImage(@PathVariable Long user_id,
                                      @PathVariable Long image_id,
                                      @PathVariable String tag_name) {
        try {
            imageTagService.save(image_id, tag_name, user_id);
            return new ResponseEntity<>("image tag was successfully added", HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // delete image tag
    @Transactional
    @DeleteMapping("/{user_id}/image/{image_id}/image_tag/{tag_name}")
    public ResponseEntity<?> deleteImageTag(@PathVariable Long user_id,
                                            @PathVariable Long image_id,
                                            @PathVariable String tag_name) {
        try {
            imageTagService.delete(image_id, tag_name, user_id);
            return new ResponseEntity<>("image tag was successfully deleted", HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // delete image rating
    @Transactional
    @DeleteMapping("/{user_id}/image/{image_id}/rating")
    public ResponseEntity<?> deleteImageRating(@PathVariable Long user_id,
                                               @PathVariable Long image_id) {
        try {
            imageRaitingService.delete(image_id, user_id);
            return new ResponseEntity<>("image tag was successfully deleted", HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // update or create rating of image by some user
    @Transactional
    @PutMapping("/{user_id}/image/{image_id}/rating")
    public ResponseEntity<?> addImageRating(@PathVariable Long user_id,
                                            @PathVariable Long image_id,
                                            @RequestParam Long rating_number) {
        try {
            imageRaitingService.save(image_id, user_id, rating_number);
            return new ResponseEntity<>("rating was successfully updated", HttpStatus.OK);

        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

//    @DeleteMapping("/{user_id}/image/{image_id}/rating")
//    public ResponseEntity<?> deleteImageRating(@PathVariable Long user_id,
//                                            @PathVariable Long image_id){
//        try {
//            imageRaitingService.delete(user_id, image_id);
//            return new ResponseEntity<>("rating was successfully deleted", HttpStatus.OK);
//
//        } catch (Exception exception) {
//            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
//        }
//
//    }

}
