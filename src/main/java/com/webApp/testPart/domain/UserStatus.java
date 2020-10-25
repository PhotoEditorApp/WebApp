package com.webApp.testPart.domain;

import javafx.css.Size;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="account_status")
public class UserStatus  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long UserId;

    public UserStatus() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }
}
