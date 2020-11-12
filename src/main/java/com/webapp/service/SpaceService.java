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
<<<<<<< HEAD
    final UserRepository userRepository;
    final SpaceRepository spaceRepository;

    public SpaceService(UserRepository userRepository, SpaceRepository spaceRepository) {
        this.userRepository = userRepository;
        this.spaceRepository = spaceRepository;
    }
=======
    @Autowired
    UserRepository userRepository;
    @Autowired
    SpaceRepository spaceRepository;
>>>>>>> 86d9921633c2ebad026850dde333876b5550b600


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
