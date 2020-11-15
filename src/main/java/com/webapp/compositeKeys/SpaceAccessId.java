package com.webapp.compositeKeys;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SpaceAccessId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long spaceId;
    private Long userId;

    public SpaceAccessId() {}

    public SpaceAccessId(Long spaceId, Long userId) {
        this.spaceId = spaceId;
        this.userId = userId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpaceAccessId that = (SpaceAccessId) o;
        return Objects.equals(spaceId, that.spaceId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spaceId, userId);
    }
}

