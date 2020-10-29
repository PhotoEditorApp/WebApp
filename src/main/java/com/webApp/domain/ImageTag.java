package com.webApp.testPart.domain;

import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

