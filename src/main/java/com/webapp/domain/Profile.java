package com.webapp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="profile")
public class Profile  implements Serializable {
    @Id
    private Long user_id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private Boolean acceptTermsOfService;

    public Profile() {
    }

    public Profile(UserAccount user){
        this.user_id = user.getId();
        this.email = user.getEmail();
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAcceptTermsOfService() {
        return acceptTermsOfService;
    }

    public void setAcceptTermsOfService(Boolean acceptTermsOfService) {
        this.acceptTermsOfService = acceptTermsOfService;
    }


}