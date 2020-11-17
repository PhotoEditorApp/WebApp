package com.webapp.controllers;

import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;
//import com.webapp.json.UserSpacesMessage;
//import com.webapp.repositories.SpaceRepository;
//import com.webapp.service.SpaceService;
import com.webapp.enums.AccessType;
import com.webapp.json.CreateSpaceRequest;
import com.webapp.json.SpaceMessage;
import com.webapp.service.SpaceService;
import com.webapp.service.UserAccountService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
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

    // get all available spaces of user by params
    @GetMapping
    public HttpEntity<? extends Serializable> getSpacesByUserId(@RequestParam Long user_id,
                                                                @RequestParam(required = false) Optional<AccessType> type){
        try{
            // form spaces, which convert to json response
            ArrayList<SpaceMessage> spaces = new ArrayList<>();
            spaceService.getSpacesByUserId(user_id, type).forEach(space -> {
                SpaceMessage spaceMessage = new SpaceMessage();
                spaceMessage.setId(space.getId());
                spaceMessage.setUserId(space.getUser().getId());
                spaceMessage.setName(space.getName());
                spaceMessage.setDescription(space.getDescription());
                spaceMessage.setCreatedTime(space.getCreatedTime());
                spaceMessage.setModifiedTime(space.getModifiedTime());
                spaces.add(spaceMessage);
            });


            return new ResponseEntity<>(spaces, HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // get space by id
    @GetMapping("{id}")
    public HttpEntity<? extends Serializable> getSpace(@PathVariable Long id){
        try {
            Space space = spaceService.getById(id);
            SpaceMessage spaceMessage = new SpaceMessage();
            spaceMessage.setId(space.getId());
            spaceMessage.setUserId(space.getUser().getId());
            spaceMessage.setName(space.getName());
            spaceMessage.setDescription(space.getDescription());
            spaceMessage.setCreatedTime(space.getCreatedTime());
            spaceMessage.setModifiedTime(space.getModifiedTime());
            return new ResponseEntity(spaceMessage, HttpStatus.OK);

        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // get all users, which are connected with space
    @GetMapping("/user")
    public HttpEntity<? extends Serializable> getUsersBySpace(@RequestParam Long space_id){
        try {
            return new ResponseEntity<>(userAccountService.getUsersBySpaceId(space_id), HttpStatus.OK);
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

    // delete space and all spaceAccess, which are connected with it
    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteSpace(@PathVariable Long id){
        try {
            spaceService.deleteById(id);
            return new ResponseEntity<>("The space has been deleted", HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
