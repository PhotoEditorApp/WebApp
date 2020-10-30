package com.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "image_tag")
public class ImageTag implements Serializable {
    @Id
    private Long userTagId;
    @Id
    private Long imageId;

    public ImageTag() {
    }

    public Long getUserTagId() {
        return userTagId;
    }

    public void setUserTagId(Long userTagId) {
        this.userTagId = userTagId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}

