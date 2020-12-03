package com.webapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="profile")
@JsonIgnoreProperties({"userAccount"})
public class Profile  implements Serializable {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private Boolean acceptTermsOfService;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    @MapsId
    private UserAccount userAccount;

    public Profile() {
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Profile(UserAccount user){
        this.id = user.getId();
        this.email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long user_id) {
        this.id = user_id;
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