package com.webapp.repositories;

import com.webapp.domain.Profile;
import com.webapp.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.Nullable;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Override
    Optional<Profile> findById(Long id);

    Optional<Profile> findByEmail(String email);
}