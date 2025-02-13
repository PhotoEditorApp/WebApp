package com.webapp.service;

import com.webapp.domain.Space;
import com.webapp.domain.SpaceAccess;
import com.webapp.domain.UserAccount;
import com.webapp.domain.UserImage;
import com.webapp.enums.AccessType;
import com.webapp.json.ImageResponse;
import com.webapp.repositories.ImageTagRepository;
import com.webapp.repositories.SpaceRepository;
import com.webapp.repositories.UserImageRepository;
import com.webapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class SpaceService {

    private final UserRepository userRepository;
    private final SpaceRepository spaceRepository;
    private final SpaceAccessService spaceAccessService;
    private final UserImageService imageService;
    private final ImageTagRepository imageTagRepository;
    private final UserImageRepository userImageRepository;
    private final UserImageService userImageService;

    @Autowired
    public SpaceService(UserRepository userRepository, SpaceRepository spaceRepository,
                        SpaceAccessService spaceAccessService, UserImageService imageService,
                        ImageTagRepository imageTagRepository, UserImageRepository userImageRepository, UserImageService userImageService) {
        this.userRepository = userRepository;
        this.spaceRepository = spaceRepository;
        this.spaceAccessService = spaceAccessService;
        this.imageService = imageService;
        this.imageTagRepository = imageTagRepository;
        this.userImageRepository = userImageRepository;
        this.userImageService = userImageService;
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

    // if user is owner -> delete space + spaceAccess
    // user is editor, viewer -> delete spaceAccess
    public void deleteByUserIdAndSpaceId(Long spaceId, Long userId) throws Exception {
        Optional<SpaceAccess> spaceAccess = spaceAccessService.findSpaceAccess(spaceId, userId);
        if (spaceAccess.isPresent()){
            // delete space access + image tags from this user
            if (spaceAccess.get().getType().equals(AccessType.CREATOR)) {
                // if owner delete space -> delete space
                deleteSpace(spaceAccess.get().getId().getSpaceId());
                spaceRepository.flush();
            }
            else{
                // else delete only access
                spaceAccessService.delete(spaceAccess.get());
            }
        }
        else{
            throw new Exception("cannot find space Access");
        }

    }

    public void deleteSpace(Long spaceId) throws Exception {
        Optional<Space> space = spaceRepository.findById(spaceId);
        space.orElseThrow(() -> new Exception("cannot find space"));
        // delete images
        space.get().getUserImages().forEach(userImageService::delete);
        // delete accesses
        space.get().getSpaceAccesses().forEach(spaceAccessService::delete);
        userImageRepository.flush();
        // delete space
        spaceRepository.delete(space.get());
        spaceRepository.flush();
    }


    // get all images from space
    public ArrayList<ImageResponse> getImages(Long space_id) throws Exception{
        Optional<Space> space = spaceRepository.findById(space_id);
        ArrayList<ImageResponse> imageResponses = new ArrayList<>();
        if (space.isPresent()){
            // transform userImage -> ImageRespons
            for (UserImage userImage : space.get().getUserImages()) {
                ArrayList<String> tags = userImageService.getTagsByImage(userImage.getId());
                imageResponses.add(new ImageResponse(userImage, tags));
            }
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

    // delete all space accesses + images
    public void deleteById(Long id) throws Exception{
        Optional<Space> space = spaceRepository.findById(id);

        if (space.isPresent()){
            // delete all spaceAccess, which relates with it
            space.get().getSpaceAccesses()
                      .forEach(spaceAccessService::delete);
            // delete all images, which relates with it
            space.get().getUserImages()
                      .forEach(imageService::delete);

            spaceRepository.deleteById(id);
        }
        else {
            throw new Exception("cannot find space");
        }
    }

}
