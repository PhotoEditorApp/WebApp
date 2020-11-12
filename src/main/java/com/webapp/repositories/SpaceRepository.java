package com.webapp.repositories;

import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceRepository extends JpaRepository<Space, Long> {
    Space findSpaceById(Long id);
    List<Space> findByUser(UserAccount userAccount);

}
