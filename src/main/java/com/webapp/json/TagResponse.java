package com.webapp.json;

import com.webapp.domain.UserAccount;
import com.webapp.domain.UserTag;

import javax.persistence.*;
import java.io.Serializable;

public class TagResponse implements Serializable {
    private Long id;
    private String name;
    private Long userId;

    TagResponse(){}
    public TagResponse(UserTag tag){
        this.id = tag.getId();
        this.name = tag.getName();
        this.userId = tag.getUser().getId();
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
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
