package com.webapp.service;

import com.webapp.domain.Profile;
import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;
import com.webapp.domain.UserAccountSecurity;
import com.webapp.enums.AccessType;
import com.webapp.json.UserAccountResponseMessage;
import com.webapp.repositories.SpaceRepository;
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
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private SpaceRepository spaceRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new UserAccountSecurity(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found")));
    }

    // finding user by email
    public Optional<UserAccount> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    // sign up user by email and password
    public void userSignUp(String email, String password) throws Exception {
        if (userRepository.findByEmail(email).isEmpty()) {
            UserAccount user = new UserAccount();
            user.setEmail(email);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setActivationCode(UUID.randomUUID().toString());
            // must be false in general
            user.setEnabled(true);
            user.setPassword_hash_algorithm("sha-512");
            user.setRegistration_time(
                    LocalTime.parse("00:00:00").format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            );

            // save user and profile
            userRepository.save(user);
            profileService.save(new Profile(user));
            // send confirmation letter to email
            if (!user.getEmail().isEmpty()) {
                String message = String.format(
                        "Hello, %s! \n" +
                                "Welcome to PhotoEditorApp. " +
                                "Follow the <a href=http://localhost:8080/user/activate/%s>" +
                                "link" +
                                "</a>.",
                        user.getEmail(),
                        user.getActivationCode()
                );

//                mailSender.send(user.getEmail(), "Activation code", message);
            }
        }else throw new Exception("The user already exists");
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

    public boolean exists(Long user_id){
        return userRepository.existsById(user_id);
    }

    // get user by id
    public Optional<UserAccount> findById(Long id){
        return userRepository.findById(id);
    }

    // get all users, which are connected with space
    public ArrayList<UserAccountResponseMessage> getUsersBySpaceId(Long space_id) throws Exception{
        // get space or throw expression
        Space space = spaceService.getById(space_id);

        ArrayList<UserAccountResponseMessage> userAccountResponseMessages = new ArrayList<>();

        // go through all spaces of user and add it to returned list.
        space.getSpaceAccesses()
                .forEach(spaceAccess ->
                        userAccountResponseMessages
                                .add(new UserAccountResponseMessage(spaceAccess.getUser())));

        return userAccountResponseMessages;
    }



}