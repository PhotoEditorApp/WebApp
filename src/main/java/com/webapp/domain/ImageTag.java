package com.webapp.domain;

import com.webapp.compositeKeys.ImageTagId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "image_tag")
public class ImageTag implements Serializable {
    @EmbeddedId
    ImageTagId imageTagId;

    @ManyToOne
    @MapsId("imageId")
    @JoinColumn(name = "image_id")
    private UserImage image;


    public ImageTag() {
    }

    public ImageTag(UserImage userImage, String tagName) {
        this.imageTagId = new ImageTagId(userImage.getId(), tagName);
        this.image = userImage;
    }

    public ImageTagId getImageTagId() {
        return imageTagId;
    }

    public void setImageTagId(ImageTagId imageTagId) {
        this.imageTagId = imageTagId;
    }

    public UserImage getImage() {
        return image;
    }

    public void setImage(UserImage image) {
        this.image = image;
    }

//
//        @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ImageTag imageTag = (ImageTag) o;
//        return Objects.equals(tagId, imageTag.tagId) &&
//                Objects.equals(, imageTag.imageId);
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(tagId, imageId);
//    }
}

