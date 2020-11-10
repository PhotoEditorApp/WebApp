package com.webapp.service;

import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;
import com.webapp.repositories.SpaceRepository;
import com.webapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SpaceRepository spaceRepository;


    public Space getSpaceById(Long id){
        return spaceRepository.findSpaceById(id);
    }

    // ищем spaces у пользователя. Кидаем исключение, если нет такого id.
    public List<Space> getSpacesByUserId(Long id) throws Exception {
        UserAccount userAccount = userRepository.findById(id).orElseThrow(()-> new Exception("Cannot find user"));
        return spaceRepository.findByUser(userAccount);
    }

    public void save(Space space){
        spaceRepository.save(space);
    }

    public void deleteById(Long id){
        spaceRepository.deleteById(id);
    }

}
