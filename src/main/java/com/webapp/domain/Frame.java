package com.webapp.domain;

import javax.persistence.*;

@Entity
@Table(name="frame")
public class Frame implements Picture{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String path;
    private String previewPath;

    public Frame(){}

    public Frame(String name){
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getPreviewPath() {
        return previewPath;
    }

    public void setPreviewPath(String preview_path) {
        this.previewPath = preview_path;
    }
}
