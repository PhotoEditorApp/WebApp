package com.webapp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="image")
public class UserImage implements Serializable, Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private UserAccount user;
    @OneToMany(mappedBy="image")
    private Set<ImageTag> tags = new HashSet<>();
    private String path;
    private Date createTime;
    private Long size;
    private Float rating;

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    private Date modifiedTime;
    private String previewPath;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "avg_color_id", referencedColumnName = "id")
    private AverageColor averageColor;
    @ManyToOne
    @JoinColumn(name="space_id")
    private Space space;
    @OneToMany(mappedBy = "image")
    private Set<ImageRating> ratings = new HashSet();

    public Set<ImageRating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<ImageRating> ratings) {
        this.ratings = ratings;
    }

    public Set<ImageTag> getTags() {
        return tags;
    }

    public void setTags(Set<ImageTag> tags) {
        this.tags = tags;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public UserImage() {
    }

    public UserImage(UserAccount user, String path,
                     Date createTime, Date modifiedTime,
                     Long size, AverageColor averageColor,
                     String name, Space space){
        this.user = user;
        this.createTime = createTime;
        this.modifiedTime = modifiedTime;
        this.name = name;
        this.path = path;
        this.size = size;
        this.averageColor = averageColor;
        this.space = space;
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

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    @Override
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

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public AverageColor getAverageColor() {
        return averageColor;
    }

    public void setAverageColor(AverageColor averageColor) {
        this.averageColor = averageColor;
    }

    @Override
    public String getPreviewPath() {
        return previewPath;
    }

    public void setPreviewPath(String preview_path) {
        this.previewPath = preview_path;
    }
}
