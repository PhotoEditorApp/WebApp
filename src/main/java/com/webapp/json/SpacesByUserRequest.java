package com.webapp.json;

import com.webapp.enums.AccessType;

import java.io.Serializable;

public class SpacesByUserRequest implements Serializable {
    private Long userId;
    private AccessType type;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AccessType getType() {
        return type;
    }

    public void setType(AccessType type) {
        this.type = type;
    }
}
