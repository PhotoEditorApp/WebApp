package com.webapp.compositeKeys;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ImageTagId implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long tagId;
    private Long imageId;


    public ImageTagId() {
    }

    public ImageTagId(Long imageId, Long tagId) {
        this.imageId = imageId;
        this.tagId = tagId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
