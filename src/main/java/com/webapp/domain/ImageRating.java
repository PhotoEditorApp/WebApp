package com.webapp.domain;

import com.webapp.compositeKeys.ImageRatingId;
import com.webapp.compositeKeys.ImageTagId;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "image_rating")
public class ImageRating implements Serializable {
    @EmbeddedId
    ImageRatingId imageRatingId;

    @ManyToOne
    @MapsId("imageId")
    @JoinColumn(name = "image_id")
    private UserImage image;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    public ImageRating(){};

    private Long rating;

    public ImageRating(UserImage userImage, UserAccount userAccount, Long rating){
        this.image = userImage;
        this.userAccount = userAccount;
        this.rating = rating;
        this.imageRatingId = new ImageRatingId(userImage.getId(), userAccount.getId());
    }


    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public ImageRatingId getImageRatingId() {
        return imageRatingId;
    }

    public void setImageRatingId(ImageRatingId imageRatingId) {
        this.imageRatingId = imageRatingId;
    }

    public UserImage getImage() {
        return image;
    }

    public void setImage(UserImage image) {
        this.image = image;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
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

