package com.webapp.compositeKeys;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ImageRatingId implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long imageId;
    private Long userId;

    public ImageRatingId() {}

    public ImageRatingId(Long imageId, Long userId) {
        this.imageId= imageId;
        this.userId = userId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageRatingId that = (ImageRatingId) o;
        return Objects.equals(imageId, that.imageId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, userId);
    }
}
