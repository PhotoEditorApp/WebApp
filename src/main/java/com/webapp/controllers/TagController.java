package com.webapp.controllers;


import com.webapp.json.TagRequest;
import com.webapp.service.TagService;
import com.webapp.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tag")
public class TagController {
    private final TagService tagService;
    private final UserAccountService userAccountService;

    public TagController(TagService tagService, UserAccountService userAccountService) {
        this.tagService = tagService;
        this.userAccountService = userAccountService;
    }

    // add new tag
    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody TagRequest tagRequest){
        try {
            tagService.save(tagRequest.getName(), tagRequest.getUserId());
            return new ResponseEntity<>("tag has been successfully created",HttpStatus.OK);
        }
        catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }
    }


    public UserAccountService getUserAccountService() {
        return userAccountService;
    }

    public TagService getTagService() {
        return tagService;
    }
}
