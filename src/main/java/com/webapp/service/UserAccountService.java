package com.webapp.service;

import com.webapp.domain.UserAccount;
import com.webapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserAccountService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // email instead of username
        return userRepository.findByEmail(email);
    }

    public UserAccount findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void userSignUp(@RequestBody UserAccount userAccount){
        UserAccount userAccountFromDB = userRepository.findByEmail(userAccount.getEmail());

        if (userAccountFromDB == null) {
            userAccount.setPassword(bCryptPasswordEncoder.encode(userAccount.getPassword()));
            userAccount.setActivationCode(UUID.randomUUID().toString());
            userAccount.setEnabled(false);
            userAccount.setPassword_hash_algorithm("sha-512");
            userAccount.setRegistration_time(
                    LocalTime.parse("00:00:00").format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            );

            userRepository.save(userAccount);

            if (!userAccount.getEmail().isEmpty()) {
                String message = String.format(
                        "Hello, %s! \n" +
                                "Welcome to PhotoEditorApp. " +
                                "Follow the <a href=http://localhost:8080/users/activate/%s>" +
                                "link" +
                                "</a>.",
                        userAccount.getUsername(),
                        userAccount.getActivationCode()
                );

                mailSender.send(userAccount.getEmail(), "Activation code", message);
            }
        }
    }

    public boolean activateUser(String code) {
        UserAccount userAccount = userRepository.findByActivationCode(code);
        if (userAccount == null) {
            return false;
        }

        userAccount.setActivationCode(null);
        userAccount.setEnabled(true);
        userAccount.setRegistration_time(
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );
        userRepository.save(userAccount);
        return true;
    }

    public Optional<UserAccount> findById(Long id){
        return userRepository.findUserAccountById(id);
    }
}