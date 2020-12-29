package com.webapp.repositories;

import com.webapp.domain.Space;
import com.webapp.domain.UserImage;
import com.webapp.enums.AccessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Long> {
    Optional<Space> findById(Long id);

    @Query(
            value = "SELECT s FROM Space s LEFT JOIN SpaceAccess sa ON sa.space.id = s.id " +
                    "WHERE sa.user.id = ?1"
    )

    List<Space> findByUser(Long user_id);
    @Query(
            value = "SELECT s FROM Space s LEFT JOIN SpaceAccess sa ON sa.space.id = s.id " +
                    "WHERE sa.user.id = ?1 and sa.type = ?2"
    )
    List<Space> findByUser(Long user_id, AccessType type);

    // get all images, which are contained by space

    void deleteById(Long id);
}
