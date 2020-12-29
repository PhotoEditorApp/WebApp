package com.webapp.json;

import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;


public class CreateSpaceRequest implements Serializable {

    public CreateSpaceRequest() {}

    private String name;
    private String description;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private int color;

    public CreateSpaceRequest(String name, String description, int color) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
