package com.webapp.json;

import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

public class SpaceResponse implements Serializable{
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private Date createdTime;
    private Date modifiedTime;
    private Integer color;


    public SpaceResponse(Space space) {
        this.id = space.getId();
        this.userId = space.getUser().getId();
        this.name = space.getName();
        this.description = space.getDescription();
        this.createdTime = space.getCreatedTime();
        this.modifiedTime = space.getModifiedTime();
        this.color = space.getColor();
    }

    public SpaceResponse(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}