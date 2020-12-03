package com.webapp.repositories;

import com.webapp.compositeKeys.ImageRatingId;
import com.webapp.domain.ImageRating;
import com.webapp.domain.UserAccount;
import com.webapp.domain.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRatingRepository extends JpaRepository<ImageRating, ImageRatingId> {
    Optional<ImageRating> findByImageRatingId(ImageRatingId imageRatingId);
}
