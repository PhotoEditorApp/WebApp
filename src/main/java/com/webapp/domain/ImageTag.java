package com.webapp.domain;

import com.webapp.compositeKeys.ImageTagId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "image_tag")
public class ImageTag implements Serializable {
    @EmbeddedId
    ImageTagId imageTagId;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "user_tag_id")
    private UserTag tag;
    @ManyToOne
    @MapsId("imageId")
    @JoinColumn(name = "image_id")
    private UserImage image;

    public ImageTag() {
    }

    public ImageTag(UserImage userImage, UserTag userTag) {
        this.imageTagId = new ImageTagId(userImage.getId(), userTag.getId());
        this.tag = userTag;
        this.image = userImage;
    }

    public ImageTagId getImageTagId() {
        return imageTagId;
    }

    public void setImageTagId(ImageTagId imageTagId) {
        this.imageTagId = imageTagId;
    }

    public UserTag getTag() {
        return tag;
    }

    public void setTag(UserTag tag) {
        this.tag = tag;
    }

    public UserImage getImage() {
        return image;
    }

    public void setImage(UserImage image) {
        this.image = image;
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ImageTag imageTag = (ImageTag) o;
//        return Objects.equals(tagId, imageTag.tagId) &&
//                Objects.equals(imageId, imageTag.imageId);
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(tagId, imageId);
//    }
}

