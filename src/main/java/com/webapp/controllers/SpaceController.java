package com.webapp.controllers;

import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;
//import com.webapp.json.UserSpacesMessage;
//import com.webapp.repositories.SpaceRepository;
//import com.webapp.service.SpaceService;
import com.webapp.json.SpaceMessage;
import com.webapp.json.SpacesByUserRequest;
import com.webapp.service.SpaceService;
import com.webapp.service.UserAccountService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * Этот класс будет реализовывать API для работы с пространствами пользователя
 */

@RestController
@RequestMapping
public class SpaceController {
    // по логике URL должен содержать еще id конкретного пользователя
    // этот момент необходимо еще уточнить
    final SpaceService spaceService;
    final UserAccountService userAccountService;

    public SpaceController(SpaceService spaceService, UserAccountService userAccountService) {
        this.spaceService = spaceService;
        this.userAccountService = userAccountService;
    }


    @GetMapping("/spaces/{user_id}")
    public HttpEntity<? extends Serializable> getSpacesByUserId(@PathVariable Long user_id){
        try{
            // создаём список мест
            ArrayList<SpaceMessage> spaces = new ArrayList<>();
            // проходим по полученным space и переводим их в SpaceJSON
            spaceService.getSpacesByUserId(user_id).forEach(space ->{
                SpaceMessage spaceMessage = new SpaceMessage();
                spaceMessage.setId(space.getId());
                spaceMessage.setUserId(user_id);
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




    @PutMapping("spaces/{user_id}")
    public ResponseEntity<String> createSpaceByUserId(@PathVariable Long user_id, @RequestBody SpaceMessage spaceFromAndroid){
        // Пост запрос на создание пространства по ид пользователя
        Optional<UserAccount> user = userAccountService.findById(user_id);
       if (user.isPresent()){
            Space space = new Space();
            space.setName(spaceFromAndroid.getName());
            space.setDescription(spaceFromAndroid.getDescription());
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


    @PostMapping("/space")
    public ResponseEntity<String> getSpacesByUserAndAccessType(@RequestBody SpacesByUserRequest spacesByUserRequest){
        return null;

    }
    //Коммент для коммита
    @DeleteMapping("space/{id}")
    public ResponseEntity<String> deleteSpace(@PathVariable Long id){
        try {
            spaceService.deleteById(id);
            return new ResponseEntity<>("The space has been deleted", HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>("Cannot find space", HttpStatus.NOT_FOUND);
        }
    }
}
