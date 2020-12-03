package com.webapp.repositories;

import com.webapp.compositeKeys.ImageTagId;
import com.webapp.domain.ImageTag;
import com.webapp.domain.UserImage;
import com.webapp.domain.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageTagRepository extends JpaRepository<ImageTag, ImageTagId> {
    void deleteImageTagByImageTagId(ImageTagId imageTagId);

}
