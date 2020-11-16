package com.webapp.json;

import com.webapp.domain.UserAccount;

import java.io.Serializable;

public class UserAccountResponseMessage implements Serializable {
    private Long id;
    private String email;
    private boolean enabled;
    private String registration_time;

    public UserAccountResponseMessage(){}

    public UserAccountResponseMessage(UserAccount user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.enabled = user.isEnabled();
        this.registration_time = user.getRegistration_time();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRegistration_time() {
        return registration_time;
    }

    public void setRegistration_time(String registration_time) {
        this.registration_time = registration_time;
    }
}
