package com.webapp.repositories;

import com.webapp.compositekeys.ImageRatingId;
import com.webapp.domain.ImageRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRatingRepository extends JpaRepository<ImageRating, ImageRatingId> {
    Optional<ImageRating> findByImageRatingId(ImageRatingId imageRatingId);
}
