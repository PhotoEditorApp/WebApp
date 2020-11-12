package com.webapp.repositories;

import com.webapp.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByEmail(String email);

    Optional<UserAccount> findById(Long id);

    UserAccount findByActivationCode(String code);
}
