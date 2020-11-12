package com.webapp.repositories;

import com.webapp.domain.Profile;
import com.webapp.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Override
    Optional<Profile> findById(Long id);
}