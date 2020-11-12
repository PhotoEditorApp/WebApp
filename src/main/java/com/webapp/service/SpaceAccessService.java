package com.webapp.service;

import com.webapp.compositeKeys.SpaceAccessId;
import com.webapp.domain.Space;
import com.webapp.domain.SpaceAccess;
import com.webapp.domain.UserAccount;
import com.webapp.enums.AccessType;
import com.webapp.repositories.SpaceAccessRepository;
import com.webapp.repositories.SpaceRepository;
import com.webapp.repositories.UserRepository;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpaceAccessService {
    final SpaceAccessRepository spaceAccessRepository;
    final UserRepository userRepository;
    final SpaceRepository spaceRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public SpaceRepository getSpaceRepository() {
        return spaceRepository;
    }

    public SpaceAccessService(SpaceAccessRepository spaceAccessRepository, UserRepository userRepository, SpaceRepository spaceRepository) {
        this.spaceAccessRepository = spaceAccessRepository;
        this.userRepository = userRepository;
        this.spaceRepository = spaceRepository;
    }

    public void setAccessBetweenUserAndSpace(Long user_id, Long space_id, AccessType accessType) throws Exception  {
        Optional<UserAccount> user = userRepository.findById(user_id);
        Optional<Space> space = spaceRepository.findById(space_id);

        // нет такого пользователя
        user.orElseThrow(() -> new Exception("cannot find user"));
        // нет такого space
        space.orElseThrow(() -> new Exception("cannot find space"));
        // пытаемся установить Creator type
        // creator type устанавливается только при создании папки
        if (accessType == AccessType.CREATOR){
            throw new Exception("cannot set CREATOR type by this request");
        }

        // ищем в SpaceAccess, иначе создаём свою
        SpaceAccess spaceAccess = spaceAccessRepository
                                      .findSpaceAccessById_UserIdAndIdSpaceId(user_id, space_id)
                                      .orElseGet(() -> new SpaceAccess(new SpaceAccessId(user_id, space_id), user.get(), space.get()));

        if (spaceAccess.getType() == AccessType.CREATOR){
            throw new Exception("Can't change access type between CREATOR and space");
        }


        spaceAccess.setType(accessType);
        spaceAccessRepository.save(spaceAccess);
    }

    public SpaceAccessRepository getSpaceAccessRepository() {
        return spaceAccessRepository;
    }
}
