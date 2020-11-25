package com.webapp.repositories;

import com.webapp.compositeKeys.ImageTagId;
import com.webapp.domain.AverageColor;
import com.webapp.domain.ImageTag;
import com.webapp.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AverageColorRepository extends JpaRepository<AverageColor, Long> {
    Optional<AverageColor> findByRgb(int rgb);
}