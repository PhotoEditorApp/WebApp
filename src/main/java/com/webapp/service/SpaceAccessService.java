package com.webapp.service;

import com.webapp.compositeKeys.SpaceAccessId;
import com.webapp.domain.Space;
import com.webapp.domain.SpaceAccess;
import com.webapp.domain.UserAccount;
import com.webapp.enums.AccessType;
import com.webapp.repositories.ImageTagRepository;
import com.webapp.repositories.SpaceAccessRepository;
import com.webapp.repositories.SpaceRepository;
import com.webapp.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpaceAccessService {
    @Lazy
    final private SpaceAccessRepository spaceAccessRepository;
    final private UserRepository userRepository;
    final private SpaceRepository spaceRepository;
    final private UserImageService userImageService;
    final private ImageTagRepository imageTagRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public SpaceRepository getSpaceRepository() {
        return spaceRepository;
    }

    public SpaceAccessService(SpaceAccessRepository spaceAccessRepository, UserRepository userRepository,
                              SpaceRepository spaceRepository, UserImageService userImageService, ImageTagRepository imageTagRepository) {
        this.spaceAccessRepository = spaceAccessRepository;
        this.userRepository = userRepository;
        this.spaceRepository = spaceRepository;
        this.userImageService = userImageService;
        this.imageTagRepository = imageTagRepository;
    }

    // check if user has access to the image
    public boolean isUserHasAccessToImage(Long userId, Long imageId, AccessType type) throws Exception {
        // find space_id of image
        Long spaceId = userImageService.getSpace(imageId).getId();
        Optional<SpaceAccess> spaceAccess = findSpaceAccess(spaceId, userId);
        if (spaceAccess.isPresent()) {
            return spaceAccess.get().getType().equals(type);
        } else {
            return false;
        }
    }

    public Optional<SpaceAccess> findSpaceAccess(Long spaceId, Long userId) throws Exception{
        Optional<UserAccount> userAccount = userRepository.findById(userId);
        Optional<Space> space = spaceRepository.findById(spaceId);
        userAccount.orElseThrow(()-> new Exception("cannot find user"));
        space.orElseThrow(()-> new Exception("cannot find user"));
        return spaceAccessRepository.findSpaceAccessById_UserIdAndIdSpaceId(userId, spaceId);
    }

    public void setAccessBetweenUserAndSpace(Long userId, Long spaceId, AccessType accessType) throws Exception {
        Optional<UserAccount> user = userRepository.findById(userId);
        Optional<Space> space = spaceRepository.findById(spaceId);

        // нет такого пользователя
        user.orElseThrow(() -> new Exception("cannot find user"));
        // нет такого space
        space.orElseThrow(() -> new Exception("cannot find space"));

        // ищем в SpaceAccess, иначе создаём свою
        SpaceAccess spaceAccess = spaceAccessRepository
                .findSpaceAccessById_UserIdAndIdSpaceId(userId, spaceId)
                .orElseGet(() -> new SpaceAccess(new SpaceAccessId(userId, spaceId), user.get(), space.get()));

        if (spaceAccess.getType() == AccessType.CREATOR) {
            throw new Exception("Can't change access type between CREATOR and space");
        }


        spaceAccess.setType(accessType);
        spaceAccessRepository.save(spaceAccess);
    }

    // delete space access and image tags
    public void delete(SpaceAccess spaceAccess) {
        // delete tags
//        spaceAccess.getSpace().getUserImages()
//                .forEach(userImage -> userImage.getImageTags()
//                        .forEach(imageTag -> {
//                            if (imageTag.getTag().getUser().equals(spaceAccess.getUser())){
//                                imageTagRepository.deleteByTag(imageTag.getTag());
//                            }
//                        }));

        // delete space acess
        spaceAccessRepository.delete(spaceAccess);
    }

    public SpaceAccessRepository getSpaceAccessRepository() {
        return spaceAccessRepository;
    }
}
