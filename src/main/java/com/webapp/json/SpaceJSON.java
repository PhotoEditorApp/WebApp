package com.webapp.json;

import com.webapp.domain.UserAccount;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

public class SpaceJSON implements Serializable{

    private String name;
    private String description;
    // цвет
//    private int co

    public SpaceJSON(){}

    public SpaceJSON(String name, String description){
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