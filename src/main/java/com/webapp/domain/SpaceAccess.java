package com.webapp.domain;
import javax.persistence.*;

import com.webapp.compositeKeys.SpaceAccessId;
import com.webapp.enums.AccessType;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "space_access")
public class SpaceAccess implements Serializable {

    @EmbeddedId
    private SpaceAccessId id = new SpaceAccessId();

    @ManyToOne
    @MapsId("userId")
    private UserAccount user;
    @ManyToOne
    @MapsId("spaceId")
    private Space space;
    @Enumerated(EnumType.STRING)
    private AccessType type;


    public AccessType getType() {
        return type;
    }

    public SpaceAccess(SpaceAccessId id, UserAccount user, Space space) {
        this.id = id;
        this.user = user;
        this.space = space;
    }
    public SpaceAccess(){}

    public void setType(AccessType type) {
        this.type = type;
    }

    public SpaceAccessId getId() {
        return id;
    }

    public void setId(SpaceAccessId id) {
        this.id = id;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpaceAccess that = (SpaceAccess) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(space, that.space) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, space, type);
    }
}
