package com.webapp.service;

import com.webapp.compositeKeys.ImageTagId;
import com.webapp.domain.*;
import com.webapp.enums.AccessType;
import com.webapp.repositories.ImageTagRepository;
import com.webapp.repositories.SpaceAccessRepository;
import com.webapp.repositories.TagRepository;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Service
public class ImageTagService {
    private final ImageTagRepository imageTagRepository;
    private final TagService tagService;
    private final UserImageService userImageService;
    private final TagRepository tagRepository;
    private final SpaceAccessService spaceAccessService;

    public ImageTagService(ImageTagRepository imageTagRepository, UserImageService userImageService,
                           TagService tagService, TagRepository tagRepository, SpaceAccessService spaceAccessService) {
        this.imageTagRepository = imageTagRepository;
        this.userImageService = userImageService;
        this.tagService = tagService;
        this.tagRepository = tagRepository;
        this.spaceAccessService = spaceAccessService;
    }

    // !!! doesn't check optional because other services do it !!!
    public void save(Long imageId, String tagName, Long userId) throws Exception {

        // check that image exists
        if (!userImageService.exists(imageId)) throw new Exception("image doesn't exist");

        // find user tag. if doesn't exist, create it
        Optional<UserTag> tag = tagService.findUserTag(userId, tagName);
        if (tag.isEmpty()) {
            tagService.save(tagName, userId);
            tag = tagService.findUserTag(userId, tagName);
        }

        // check access of user
        if (!spaceAccessService.isUserHasAccessToImage(userId, imageId, AccessType.EDITOR) &&
            !spaceAccessService.isUserHasAccessToImage(userId, imageId, AccessType.CREATOR)){
            throw new Exception("user doesn't have access to the image ");
        }

        // tag image
        if (tag.isPresent()) {
            ImageTag imageTag = new ImageTag(userImageService.findById(imageId).get(), tag.get());
            imageTagRepository.save(imageTag);
        } else {
            throw new Exception("oops, the new tag wasn't created...");
        }

    }
}
