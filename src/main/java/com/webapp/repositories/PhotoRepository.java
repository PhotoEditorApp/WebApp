package com.webapp.repositories;

import com.webapp.domain.Photo;
import com.webapp.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findById(Long id);
    Optional<Photo> findByProfile(Profile profile);
}
