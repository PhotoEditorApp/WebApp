package com.webapp.service;

import com.webapp.domain.UserAccount;
import com.webapp.domain.UserTag;
import com.webapp.json.TagResponse;
import com.webapp.repositories.TagRepository;
import io.swagger.annotations.Tag;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TagService {

    TagRepository tagRepository;
    UserAccountService userAccountService;
    UserImageService imageService;

    public TagService(TagRepository tagRepository,UserAccountService userAccountService, UserImageService imageService) {
        this.tagRepository = tagRepository;
        this.userAccountService = userAccountService;
        this.imageService = imageService;
    }

    public boolean exists(Long tagId){
        return  tagRepository.existsById(tagId);
    }

    public Optional<UserTag> findById(Long tagId){
        return tagRepository.findById(tagId);
    }



    public Optional<UserTag> findUserTag(Long userId, String tagName) throws Exception{
        Optional<UserAccount> userAccount = userAccountService.findById(userId);
        if (userAccount.isPresent()) {
            return tagRepository.findByUserAndName(userAccount.get(), tagName);
        }
        else{
            throw new Exception("cannot find user");
        }
    }

    public UserTag getByName(Long userId, String tagName) throws Exception {
        Optional<UserAccount> userAccount = userAccountService.findById(userId);
        if (userAccount.isPresent()) {
            Optional<UserTag> tag =  tagRepository.findByUserAndName(userAccount.get(), tagName);
            if (tag.isPresent()){
                return tag.get();}
            else{
                throw new Exception("cannot find tag");
            }
        }
        else{
            throw new Exception("cannot find user");
        }
    }

    public boolean existsUserTagByNameAndUser(String name, UserAccount userAccount){
        return tagRepository.existsUserTagByNameAndUser(name, userAccount);
    }

    public ArrayList<TagResponse> getUserTags(Long user_id) throws Exception {
        Optional<UserAccount> userAccount = userAccountService.findById(user_id);
        if (userAccount.isPresent()) {
            ArrayList<TagResponse> tags = new ArrayList<>();
            tagRepository.findAllByUser(userAccount.get())
                    .forEach(userTag -> tags.add(new TagResponse(userTag)));
            return tags;
        }
        else{
            throw new Exception("cannot find user");
        }
    }

    // create new tag
    public void save(String name, Long userId) throws Exception{
        Optional<UserAccount> userAccount = userAccountService.findById(userId);
        if (userAccount.isPresent()) {
            // TODO exception doesn't throw
            if (tagRepository.existsUserTagByNameAndUser(name, userAccount.get())) {
                throw new Exception("tag has already been created");
            }
            tagRepository.save(new UserTag(name, userAccount.get()));
        }
        else{
            throw new Exception("cannot find user");
        }
    }

    // delete tag of user
    public void delete(Long userId, String tagName) throws Exception {
        Optional<UserAccount> userAccount = userAccountService.findById(userId);
        userAccount.orElseThrow(() -> new Exception("cannot find user"));
        tagRepository.deleteUserTagByUserAndName(userAccount.get(), tagName);
    }




}
