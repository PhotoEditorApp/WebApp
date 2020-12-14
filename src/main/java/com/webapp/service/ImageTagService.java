package com.webapp.service;

import com.webapp.compositekeys.ImageTagId;
import com.webapp.domain.*;
import com.webapp.enums.AccessType;
import com.webapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageTagService {
    private final ImageTagRepository imageTagRepository;
    private final TagService tagService;
    private final UserImageService userImageService;
    private final SpaceAccessService spaceAccessService;
    private final UserAccountService userAccountService;

    @Autowired
    public ImageTagService(ImageTagRepository imageTagRepository, @Lazy UserImageService userImageService,
                           TagService tagService, SpaceAccessService spaceAccessService, UserAccountService userAccountService) {
        this.imageTagRepository = imageTagRepository;
        this.userImageService = userImageService;
        this.tagService = tagService;
        this.spaceAccessService = spaceAccessService;
        this.userAccountService = userAccountService;
    }

    // !!! doesn't check optional because other services do it !!!
    public void save(Long imageId, String tagName, Long userId) throws Exception {

        Optional<UserImage> userImage = userImageService.findById(imageId);

        // check that image + user exist
        userImage.orElseThrow(() -> new Exception("cannot find image"));
        if (!userAccountService.exists(userId)) throw new Exception("cannot find user");

        // check access of user
        if (!spaceAccessService.isUserHasAccessToImage(userId, imageId, AccessType.EDITOR) &&
            !spaceAccessService.isUserHasAccessToImage(userId, imageId, AccessType.CREATOR)){
            throw new Exception("user doesn't have access to the image ");
        }

        // add tag to user tags
        if (tagService.findUserTag(userId, tagName).isEmpty()) {
            tagService.save(tagName, userId);
        }

        // save tag of image
        imageTagRepository.save(new ImageTag(userImage.get(), tagName));


    }

    // delete image tag by user
    public void delete(Long imageId, String tagName, Long userId) throws Exception {
        // check access of user
        if (!spaceAccessService.isUserHasAccessToImage(userId, imageId, AccessType.EDITOR) &&
                !spaceAccessService.isUserHasAccessToImage(userId, imageId, AccessType.CREATOR)){
            throw new Exception("user doesn't have access to the image ");
        }
        imageTagRepository.deleteImageTagByImageTagId(new ImageTagId(imageId, tagName));
    }



}
