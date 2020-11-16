package com.webapp.service;

import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;
import com.webapp.enums.AccessType;
import com.webapp.repositories.SpaceRepository;
import com.webapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceService {

    final UserRepository userRepository;
    final SpaceRepository spaceRepository;
    final SpaceAccessService spaceAccessService;

    public SpaceService(UserRepository userRepository, SpaceRepository spaceRepository, SpaceAccessService spaceAccessService) {
        this.userRepository = userRepository;
        this.spaceRepository = spaceRepository;
        this.spaceAccessService = spaceAccessService;
    }



    public Space getSpaceById(Long id){
        return spaceRepository.findSpaceById(id);
    }

    // ищем spaces у пользователя. Кидаем исключение, если нет такого id.
    public List<Space> getSpacesByUserId(Long id, Optional<AccessType> type) throws Exception {
        UserAccount userAccount = userRepository.findById(id).orElseThrow(()-> new Exception("Cannot find user"));
        if (type.isPresent()) {
            return spaceRepository.findByUser(userAccount.getId(), type.get());
        }
        else{
            return spaceRepository.findByUser(userAccount.getId());
        }
    }

    public void save(Space space){
        // save new space
        spaceRepository.save(space);
        // space new SpaceAccess between space and user
        try {
            spaceAccessService.setAccessBetweenUserAndSpace(space.getUser().getId(), space.getId(), AccessType.CREATOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteById(Long id) throws Exception{
        Optional<Space> space = spaceRepository.findById(id);

        // delete all space access, which are connected with space
        if (space.isPresent()){
            space.get().getSpaceAccesses()
                      .forEach(spaceAccess -> spaceAccessService.delete(spaceAccess));
        }
        else {
            throw new Exception("cannot find space");
        }
    }

}
