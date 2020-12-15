package com.webapp.repositories;

import com.webapp.compositekeys.ImageTagId;
import com.webapp.domain.ImageTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageTagRepository extends JpaRepository<ImageTag, ImageTagId> {
    void deleteImageTagByImageTagId(ImageTagId imageTagId);

}
