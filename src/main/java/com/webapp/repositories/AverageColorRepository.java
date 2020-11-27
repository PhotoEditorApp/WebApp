package com.webapp.repositories;

import com.webapp.domain.AverageColor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AverageColorRepository extends JpaRepository<AverageColor, Long> {
    Optional<AverageColor> findByRgb(long rgb);
}