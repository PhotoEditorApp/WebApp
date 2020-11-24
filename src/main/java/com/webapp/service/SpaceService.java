package com.webapp.service;

import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;
import com.webapp.enums.AccessType;
import com.webapp.json.ImageResponse;
import com.webapp.repositories.SpaceRepository;
import com.webapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SpaceService {

    final UserRepository userRepository;
    final SpaceRepository spaceRepository;
    final SpaceAccessService spaceAccessService;
    final UserImageService imageService;

    public SpaceService(UserRepository userRepository, SpaceRepository spaceRepository, SpaceAccessService spaceAccessService, UserImageService imageService) {
        this.userRepository = userRepository;
        this.spaceRepository = spaceRepository;
        this.spaceAccessService = spaceAccessService;
        this.imageService = imageService;
    }

    // get space by id
    public Space getById(Long id) throws Exception {
        Optional<Space> space = spaceRepository.findById(id);
        if (space.isPresent()){
            return space.get();
        }
        else{
            throw new Exception("cannot find space");
        }
    }

    // get all images from space
    public ArrayList<ImageResponse> getImages(Long space_id) throws Exception{
        Optional<Space> space = spaceRepository.findById(space_id);
        ArrayList<ImageResponse> imageResponses = new ArrayList<>();
        if (space.isPresent()){
            space.get().getUserImages()
                         .forEach(userImage -> imageResponses.add(new ImageResponse(userImage)));
            return imageResponses;
        }
        else{
            throw new Exception("cannot find space");
        }

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

    // delete all space access, which are connected with space
    public void deleteById(Long id) throws Exception{
        Optional<Space> space = spaceRepository.findById(id);

        if (space.isPresent()){
            // delete all spaceAccess, which relates with it
            space.get().getSpaceAccesses()
                      .forEach(spaceAccessService::delete);
            // delete all images, which relates with it
            space.get().getUserImages()
                      .forEach(imageService::delete);
        }
        else {
            throw new Exception("cannot find space");
        }
    }

}
