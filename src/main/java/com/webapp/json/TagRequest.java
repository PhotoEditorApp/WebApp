package com.webapp.json;

import javax.swing.text.html.HTML;
import java.io.Serializable;

public class TagRequest implements Serializable {
    private Long userId;
    private String name;

    public TagRequest(){}

    public TagRequest(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
