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

    @PostMapping("/delete_space")
    public ActionMessage deleteSpace(){
        // Post запрос нас удаление space
        return new ActionMessage("The space has been deleted");
    }

    @GetMapping("/find_users_email")
    public ActionMessage findUsersByEmail(){
        // Поиск пользователей по почте
        return new ActionMessage("List of users");
    }

    @GetMapping("/get_access_id")
    public ActionMessage getAccessById(){
        // Запрос на доступ Шер папки с user_id
        return new ActionMessage("Access granted");
    }
}
