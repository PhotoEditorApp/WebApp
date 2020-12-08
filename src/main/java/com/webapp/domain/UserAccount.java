package com.webapp.domain;

import org.hibernate.annotations.Fetch;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_account")
public class UserAccount implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String password;
    private String password_hash_algorithm;

    @Column(unique=true)
    private String email;
    private String activationCode;
    private boolean enabled;
    private String registration_time;

    @OneToOne(mappedBy = "userAccount")
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @OneToMany(mappedBy="user")
    private Set<SpaceAccess> spaceAccesses = new HashSet<>();

    @OneToMany(mappedBy="user")
    private Set<UserTag> userTags = new HashSet<>();




    public Set<UserTag> getUserTags() {
        return userTags;
    }

    public void setUserTags(Set<UserTag> userTags) {
        this.userTags = userTags;
    }

    public Set<SpaceAccess> getSpaceAccesses() {
        return spaceAccesses;
    }

    public void setSpaceAccesses(Set<SpaceAccess> spaceAccesses) {
        this.spaceAccesses = spaceAccesses;
    }

    public UserAccount(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getPassword_hash_algorithm() {
        return password_hash_algorithm;
    }

    public void setPassword_hash_algorithm(String password_hash_algorithm) {
        this.password_hash_algorithm = password_hash_algorithm;
    }

    public String getRegistration_time() {
        return registration_time;
    }

    public void setRegistration_time(String registration_time) {
        this.registration_time = registration_time;
    }
}

