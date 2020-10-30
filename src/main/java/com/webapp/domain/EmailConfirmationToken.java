package com.webapp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "email_confirmation_token")
public class EmailConfirmationToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String token;
    private Date expired;

    public EmailConfirmationToken(Long userId, String token, Date expired) {
        this.userId = userId;
        this.token = token;
        this.expired = expired;
    }


    public EmailConfirmationToken() {

    }

    public EmailConfirmationToken(Long userId){
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }
}