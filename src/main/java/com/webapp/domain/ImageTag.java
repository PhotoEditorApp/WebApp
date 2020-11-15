package com.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageTag imageTag = (ImageTag) o;
        return Objects.equals(userTagId, imageTag.userTagId) &&
                Objects.equals(imageId, imageTag.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userTagId, imageId);
    }
}

