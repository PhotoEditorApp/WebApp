package com.webapp.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="image")
public class UserImage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private UserAccount user;
    private String path;

    private Date createTime;
    private Long size;
    private Date modifiedTime;
    private Long averageColorId;

    public UserImage() {
    }

    public UserImage(UserAccount user, String path,
                     Date createTime, Date modifiedTime,
                     Long size, Long averageColorId,
                     String name){
        this.user = user;
        this.averageColorId = averageColorId;
        this.createTime = createTime;
        this.modifiedTime = modifiedTime;
        this.name = name;
        this.path = path;
        this.size = size;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Long getAverageColorId() {
        return averageColorId;
    }

    public void setAverageColorId(Long averageColorId) {
        this.averageColorId = averageColorId;
    }
}
