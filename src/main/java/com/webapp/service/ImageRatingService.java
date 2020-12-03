package com.webapp.service;

import com.webapp.compositeKeys.ImageRatingId;
import com.webapp.domain.ImageRating;
import com.webapp.domain.UserAccount;
import com.webapp.domain.UserImage;
import com.webapp.enums.AccessType;
import com.webapp.repositories.ImageRatingRepository;
import com.webapp.repositories.UserImageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageRatingService {

    private final SpaceAccessService spaceAccessService;
    private final UserAccountService userAccountService;
    private final UserImageService userImageService;
    private final ImageRatingRepository imageRatingRepository;

    public ImageRatingService(SpaceAccessService spaceAccessService, UserAccountService userAccountService, UserImageService userImageService, ImageRatingRepository imageRatingRepository) {
        this.spaceAccessService = spaceAccessService;
        this.userAccountService = userAccountService;
        this.userImageService = userImageService;
        this.imageRatingRepository = imageRatingRepository;
    }


    // update or create rating of image
    public void save(Long imageId, Long userId, Long ratingNumber) throws Exception {
        Optional<UserImage> userImage = userImageService.findById(imageId);
        Optional<UserAccount> userAccount = userAccountService.findById(userId);

        // check that image + user exist + range of ratingNumber
        userImage.orElseThrow(() -> new Exception("cannot find image"));
        userAccount.orElseThrow(() -> new Exception("cannot find user"));
        if (ratingNumber < 0 || ratingNumber > 10) throw new Exception("not valid rating");

        // check access of user
        if (!spaceAccessService.isUserHasAccessToImage(userId, imageId, AccessType.EDITOR) &&
                !spaceAccessService.isUserHasAccessToImage(userId, imageId, AccessType.CREATOR)){
            throw new Exception("user doesn't have access to the image ");
        }

        Optional<ImageRating> imageRating = imageRatingRepository.findByImageRatingId(new ImageRatingId(imageId, userId));

        if (imageRating.isPresent()){
            imageRating.get().setRating(ratingNumber);
        }
        else{
            imageRating = Optional.of(new ImageRating(userImage.get(), userAccount.get(), ratingNumber));
        }

        imageRatingRepository.save(imageRating.get());
        userImage.get().setRating(userImageService.getRatingByImage(imageId));

    }


    // update or create rating of image
    public void delete(Long user_id, Long image_id) {

    }

}
