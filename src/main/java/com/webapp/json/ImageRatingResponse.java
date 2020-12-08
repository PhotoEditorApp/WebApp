package com.webapp.json;

import java.io.Serializable;

public class ImageRatingResponse implements Serializable {
    private Long userId;
    private Long imageId;
    private Long rating;

    public ImageRatingResponse(){}

    public ImageRatingResponse(Long userId, Long imageId, Long rating){
        this.userId = userId;
        this.imageId = imageId;
        this.rating = rating;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }
}
