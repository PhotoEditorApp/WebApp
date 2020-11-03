package com.webapp.json;

import java.io.Serializable;

public class SomeId implements Serializable {
    Long id;

    public SomeId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
