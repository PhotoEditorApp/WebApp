package com.webapp.compositekeys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ImageTagId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="tag_name")
    private String tagName;
    @Column(name="image_id")
    private Long imageId;


    public ImageTagId() {
    }

    public ImageTagId(Long imageId, String tagName) {
        this.imageId = imageId;
        this.tagName = tagName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
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
        ImageTagId that = (ImageTagId) o;
        return imageId.equals(that.imageId) &&
                tagName.equals(that.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, tagName);
    }
}
