package com.webapp.repositories;

import com.webapp.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByEmail(String email);

    UserAccount findByActivationCode(String code);
}
