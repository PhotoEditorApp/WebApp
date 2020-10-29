package com.webApp.testPart.domain.
import com.webapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<com.webapp.domain.UserAccount, Long> {
    com.webapp.domain.UserAccount findByUsername(String username);

    com.webapp.domain.UserAccount findByActivationCode(String code);
}