package com.webapp.repositories;

import com.webapp.compositeKeys.ImageTagId;
import com.webapp.domain.UserAccount;
import com.webapp.domain.UserTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<UserTag, Long> {
    List<UserTag> findAllByUser(UserAccount userAccount);

    boolean existsUserTagByNameAndUser(String name, UserAccount user);
    void deleteUserTagByUserAndName(UserAccount user, String name);
    boolean existsById(Long tagId);

    Optional<UserTag> findById(Long tagId);
    Optional<UserTag> findByUserAndName(UserAccount userAccount, String name);
}
