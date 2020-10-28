package com.webapp.controllers;

import com.webapp.json.ActionMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Этот класс будет реализовывать API для работы с пространствами пользователя
 */

@RestController
@RequestMapping
public class SpaceManagement {
    // по логике URL должен содержать еще id конкретного пользователя
    // этот момент необходимо еще уточнить

    @GetMapping("/spaces")
    public ActionMessage getSpacesById(){
        // Получение всех space по user_id
        return new ActionMessage("This is all available spaces");
    }

    @GetMapping("/profile")
    public ActionMessage getProfile(){
        // Запрос гет для профайл
        return new ActionMessage("Here's your profile");
    }

    @PostMapping("/profile")
    public ActionMessage postProfile(){
        // Запрос пост для профайл
        return new ActionMessage("Here's Johny");
    }

    @PostMapping("/create_space")
    public ActionMessage createSpaceById(){
        // Пост запрос на создание пространства по ид пользователя
        return new ActionMessage("The space has been created");
    }
}
