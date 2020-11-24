package com.webapp.repositories;

import com.webapp.compositeKeys.ImageTagId;
import com.webapp.domain.ImageTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageTagRepository extends JpaRepository<ImageTag, ImageTagId> {
}
