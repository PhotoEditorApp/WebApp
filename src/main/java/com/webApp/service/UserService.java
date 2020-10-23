package com.webApp.service;

import com.webApp.domain.User;
import com.webApp.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public void userSignUp(@RequestBody User user){
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setActivationCode(UUID.randomUUID().toString());
            user.setEnabled(false);
            userRepository.save(user);

            if (!user.getEmail().isEmpty()) {
                String message = String.format(
                        "Hello, %s! \n" +
                                "Welcome to PhotoEditorApp." +
                                " <a href=https://localhost:8443/users/activate/%s>" +
                                "Please, visit next link</a>",
                        user.getUsername(),
                        user.getActivationCode()
                );

                mailSender.send(user.getEmail(), "Activation code", message);
            }
        }
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }
}