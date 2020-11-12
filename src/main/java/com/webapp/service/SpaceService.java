package com.webapp.service;

import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;
import com.webapp.repositories.SpaceRepository;
import com.webapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaceService {
    final UserRepository userRepository;
    final SpaceRepository spaceRepository;

    public SpaceService(UserRepository userRepository, SpaceRepository spaceRepository) {
        this.userRepository = userRepository;
        this.spaceRepository = spaceRepository;
    }


    public Space getSpaceById(Long id){
        return spaceRepository.findSpaceById(id);
    }

    // ищем spaces у пользователя. Кидаем исключение, если нет такого id.
    public List<Space> getSpacesByUserId(Long id) throws ChangeSetPersister.NotFoundException {
        UserAccount userAccount = userRepository.findById(id)
                                                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        return spaceRepository.findByUser(userAccount);
    }

    public void save(Space space){
        spaceRepository.save(space);
        spaceRepository.flush();
    }

}
