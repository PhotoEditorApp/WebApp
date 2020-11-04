package com.webapp.json;

import java.io.Serializable;

public class SomeId implements Serializable {
    Long id;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
