package com.webapp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user_tag")
public class UserTag implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE,
                     generator = "user_tag_seq")
    @SequenceGenerator(name="user_tag_seq", sequenceName = "user_tag_id_seq", allocationSize = 1)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserAccount user;
    @OneToMany(mappedBy="tag")
    private Set<ImageTag> imageTags;

    public Set<ImageTag> getImageTags() {
        return imageTags;
    }

    public void setImageTags(Set<ImageTag> imageTags) {
        this.imageTags = imageTags;
    }

    public UserTag() {
    }

    public UserTag(String name, UserAccount user){
        this.name = name;
        this.user = user;
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

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }
}