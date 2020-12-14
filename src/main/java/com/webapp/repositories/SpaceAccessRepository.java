package com.webapp.repositories;

import com.webapp.compositekeys.SpaceAccessId;
import com.webapp.domain.SpaceAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SpaceAccessRepository extends JpaRepository<SpaceAccess, SpaceAccessId> {

    public Optional<SpaceAccess> findSpaceAccessById_UserIdAndIdSpaceId(Long user_id, Long space_id);
}