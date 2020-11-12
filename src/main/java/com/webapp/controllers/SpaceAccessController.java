package com.webapp.controllers;

import com.webapp.domain.Profile;
import com.webapp.json.ProfileRequestMessage;
import com.webapp.json.SpaceAccessRequest;
import com.webapp.service.ProfileService;
import com.webapp.service.SpaceAccessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Optional;

@RestController
@RequestMapping
public class SpaceAccessController {
    final SpaceAccessService spaceAccessService;


    public SpaceAccessController(SpaceAccessService spaceAccessService) {
        this.spaceAccessService = spaceAccessService;
    }

    @PutMapping("/space_access")
    public ResponseEntity<? extends Serializable> createSpaceAccess(@RequestBody SpaceAccessRequest spaceAccessRequest) {
        try{
            spaceAccessService.setAccessBetweenUserAndSpace(spaceAccessRequest.getUser_id(),
                                                            spaceAccessRequest.getSpace_id(),
                                                            spaceAccessRequest.getType());
            return new ResponseEntity<>("AccessType has been successfully updated or created", HttpStatus.OK);
        }
        catch(Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
