package com.webapp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="space")
public class Space implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE,
                     generator = "space_seq")
    @SequenceGenerator(name="space_seq", sequenceName = "seq_id_space", allocationSize = 1)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserAccount user;
    private String name;
    private String description;
    private int color;
    private Date createdTime;
    private Date modifiedTime;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @OneToMany(mappedBy="space")
    private Set<SpaceAccess> spaceAccesses = new HashSet<>();

    public Set<SpaceAccess> getSpaceAccesses() {
        return spaceAccesses;
    }

    public void setSpaceAccesses(Set<SpaceAccess> spaceAccesses) {
        this.spaceAccesses = spaceAccesses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Space() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
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
}
