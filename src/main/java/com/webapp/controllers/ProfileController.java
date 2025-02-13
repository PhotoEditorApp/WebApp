package com.webapp.controllers;

import com.webapp.domain.Profile;
import com.webapp.json.ProfileMessage;
import com.webapp.json.ProfileRequestMessage;
import com.webapp.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // get profile by id
    @GetMapping("{id}")
    public ResponseEntity<? extends Serializable> getProfileByUserId(@PathVariable Long id){

        Optional<Profile> profile = profileService.findById(id);

        if (profile.isPresent()){
            ProfileMessage profileMessage = new ProfileMessage();
            profileMessage.setId(profile.get().getId());
            profileMessage.setEmail(profile.get().getEmail());
            profileMessage.setFirstName(profile.get().getFirstName());
            profileMessage.setLastName(profile.get().getLastName());
            profileMessage.setFullName(profile.get().getFullName());
            profileMessage.setAcceptTermsOfService(profile.get().getAcceptTermsOfService());
            return new ResponseEntity<>(profileMessage, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("cannot find profile", HttpStatus.NOT_FOUND);
        }
    }

    // update profile by user id
    @PutMapping("{user_id}")
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

    // get profile by email
    @GetMapping("email")
    ResponseEntity<? extends Serializable> getProfileByEmail(@RequestParam String email){
        Optional<Profile> profile = profileService.findByEmail(email);
        if (profile.isPresent()){
            return new ResponseEntity<>(profile.get(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
