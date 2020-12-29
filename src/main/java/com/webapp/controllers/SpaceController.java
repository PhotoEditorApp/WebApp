package com.webapp.controllers;

import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;
//import com.webapp.json.UserSpacesMessage;
//import com.webapp.repositories.SpaceRepository;
//import com.webapp.service.SpaceService;
import com.webapp.enums.AccessType;
import com.webapp.json.CreateSpaceRequest;
import com.webapp.json.SpaceResponse;
import com.webapp.service.SpaceService;
import com.webapp.service.UserAccountService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

/**
 * Этот класс будет реализовывать API для работы с пространствами пользователя
 */

@RestController
@RequestMapping("/space")
public class SpaceController {

    final SpaceService spaceService;
    final UserAccountService userAccountService;

    public SpaceController(SpaceService spaceService, UserAccountService userAccountService) {
        this.spaceService = spaceService;
        this.userAccountService = userAccountService;
    }

    // get space by id
    @GetMapping("/{id}")
    public HttpEntity<? extends Serializable> getSpace(@PathVariable Long id){
        try {
            Space space = spaceService.getById(id);
            SpaceResponse spaceResponse = new SpaceResponse(space);
            return new ResponseEntity<>(spaceResponse, HttpStatus.OK);

        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // get all users, which are connected with space
    @GetMapping("/{space_id}/user")
    public HttpEntity<? extends Serializable> getUsersBySpace(@PathVariable Long space_id){
        try {
            return new ResponseEntity<>(userAccountService.getUsersBySpaceId(space_id), HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    // get all images, which are contained by space
    @GetMapping("/{space_id}/image")
    public ResponseEntity<? extends Serializable> getImagesBySpace(@PathVariable Long space_id){
        try{
            return new ResponseEntity<>(spaceService.getImages(space_id), HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // create new space by user_id
    @PutMapping("/{user_id}")
    public ResponseEntity<String> createSpaceByUserId(@PathVariable Long user_id, @RequestBody CreateSpaceRequest createSpaceRequest){
        Optional<UserAccount> user = userAccountService.findById(user_id);
        if (user.isPresent()){
             Space space = new Space();
             space.setName(createSpaceRequest.getName());
             space.setColor(createSpaceRequest.getColor());
             space.setDescription(createSpaceRequest.getDescription());
             space.setUser(user.get());
             space.setCreatedTime(new Date());
             space.setModifiedTime(new Date());
             spaceService.save(space);

             return new ResponseEntity<>("The Space has been successfully created", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Cannot find user", HttpStatus.NOT_FOUND);
       }
    }
}
