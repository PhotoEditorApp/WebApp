package com.webapp.controllers;

import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;
import com.webapp.json.ActionMessage;
//import com.webapp.json.UserSpacesMessage;
//import com.webapp.repositories.SpaceRepository;
//import com.webapp.service.SpaceService;
import com.webapp.json.SomeId;
import com.webapp.json.UserSpacesMessage;
import com.webapp.service.SpaceService;
import com.webapp.service.UserAccountService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Этот класс будет реализовывать API для работы с пространствами пользователя
 */

@RestController
@RequestMapping
public class SpaceController {
    final SpaceService spaceService;
    final UserAccountService userAccountService;

    public SpaceController(SpaceService spaceService, UserAccountService userAccountService) {
        this.spaceService = spaceService;
        this.userAccountService = userAccountService;
    }

    @GetMapping("/spaces")
    public ActionMessage getSpacesByUserId(){
        // Получение всех space по user_id
        try {
            final StringBuilder result = new StringBuilder();
            spaceService.getSpacesByUserId(2L).forEach((elem) -> result.append(elem.getId().toString() + " | ") );
            return new ActionMessage(result.toString());
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ActionMessage("didn't find user");
        }
    }

    @GetMapping("/profile")
    public ActionMessage getProfile(){
        // Запрос гет для профайл
        return new ActionMessage("Here's your profile");
    }


    @PostMapping(value="/profile", consumes = "application/json")
    public ActionMessage postProfile(@RequestBody SomeId id){
        // Запрос пост для профайл
        return new ActionMessage(id.getId().toString());
//        return new ActionMessage("Here's Johny");
    }

    @GetMapping("/create_space/{user_id}")
    public ActionMessage createSpaceByUserId(@PathVariable Long user_id){
        // Пост запрос на создание пространства по ид пользователя
        System.out.println("hello");
        return new ActionMessage(user_id.toString());
//        Optional<UserAccount> user = userAccountService.findById(user_id);
//        System.out.println("hello2");

//        if (user.isPresent()){
//            Space space = new Space();
//            space.setUser(user.get());
//            space.setCreatedTime(new Date());
//            space.setModifiedTime(new Date());
//            spaceService.save(space);
//            return new ActionMessage("The space has been created");
//        }
//        else{
//            return new ActionMessage("User hasn't found");
//        }
//        return new ActionMessage("hello");
    }

    @PostMapping("/delete_space")
    public ActionMessage deleteSpace(){
        // Post запрос нас удаление space
        return new ActionMessage("The space has been deleted");
    }



    @GetMapping("/get_access_id")
    public ActionMessage getAccessById(){
        // Запрос на доступ Шер папки с user_id
        return new ActionMessage("Access granted");
    }
}
