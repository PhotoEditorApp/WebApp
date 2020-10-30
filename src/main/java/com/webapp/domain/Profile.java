package com.webapp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="profile")
public class Profile  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private Boolean acceptTermsOfService;
    private Date timeZone;
    private Long userId;

    public Profile() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Date timeZone) {
        this.timeZone = timeZone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}