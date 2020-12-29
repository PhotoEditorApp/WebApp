package com.webapp.domain;

import javax.persistence.*;

@Entity
@Table(name="photo")
public class Photo implements Picture{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String path;
    private String previewPath;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="profile")
    private Profile profile;

    public Photo(){}

    public Photo(String name){
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getPreviewPath() {
        return previewPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPreviewPath(String previewPath) {
        this.previewPath = previewPath;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
