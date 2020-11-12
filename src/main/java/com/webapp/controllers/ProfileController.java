package com.webapp.controllers;

import com.webapp.domain.Profile;
import com.webapp.json.ProfileMessage;
import com.webapp.json.ProfileRequestMessage;
import com.webapp.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Optional;

@RestController
@RequestMapping
public class ProfileController {
    final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("profile/{user_id}")
    public ResponseEntity<? extends Serializable> getProfileByUserId(@PathVariable Long user_id){

        Optional<Profile> profile = profileService.findById(user_id);

        if (profile.isPresent()){
            ProfileMessage profileMessage = new ProfileMessage();
            profileMessage.setUser_id(profile.get().getUser_id());
            profileMessage.setEmail(profile.get().getEmail());
            profileMessage.setFirstName(profile.get().getFullName());
            profileMessage.setLastName(profile.get().getLastName());
            profileMessage.setFullName(profile.get().getFullName());
            profileMessage.setAcceptTermsOfService(profile.get().getAcceptTermsOfService());
            return new ResponseEntity<>(profileMessage, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("cannot find profile", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("profile/{user_id}")
    public ResponseEntity<? extends Serializable> getProfileByUserId(@PathVariable Long user_id, @RequestBody ProfileRequestMessage profileRequestMessage) {
        Optional<Profile> profile = profileService.findById(user_id);
        if (profile.isPresent()){
            profile.get().setFirstName(profileRequestMessage.getFirstName());
            profile.get().setLastName(profileRequestMessage.getLastName());
            profile.get().setFullName(profileRequestMessage.getFullName());
            profileService.save(profile.get());
           return new ResponseEntity<>("The profile has been successfully updated",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Cannot find user",HttpStatus.NOT_FOUND);
        }

    }


}
