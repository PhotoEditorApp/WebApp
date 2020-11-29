package com.webapp.json;

import com.webapp.domain.UserImage;

import java.io.Serializable;
import java.util.Date;

public class ImageResponse implements Serializable {
    private Long id;
    private String name;
    private Long user_id;
    private String path;
    private String preview_path;

    private Date createTime;
    private Long average_color;

    private Long size;
    private Date modifiedTime;

    public ImageResponse(){ }

    public ImageResponse(UserImage image){
        this.id = image.getId();
        this.name = image.getName();
        this.average_color = image.getAverageColor().getRgb();
        this.user_id = image.getUser().getId();
        this.path = image.getPath();
        this.createTime = image.getCreateTime();
        this.size = image.getSize();
        this.modifiedTime = image.getModifiedTime();
        this.preview_path = image.getPreview_path();
    }

    public Long getAverage_color() {
        return average_color;
    }

    public void setAverage_color(Long average_color) {
        this.average_color = average_color;
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

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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

    public String getPreview_path() {
        return preview_path;
    }

    public void setPreview_path(String preview_path) {
        this.preview_path = preview_path;
    }
}
