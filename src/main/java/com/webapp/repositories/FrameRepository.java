package com.webapp.repositories;

import com.webapp.domain.Frame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FrameRepository extends JpaRepository<Frame, Long> {
    Optional<Frame> findById(Long id);
}
