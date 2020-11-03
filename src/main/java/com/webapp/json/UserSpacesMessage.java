package com.webapp.json;

import com.webapp.domain.Space;
import com.webapp.domain.UserAccount;

import java.sql.Time;
import java.util.List;


public class UserSpacesMessage{
    public UserSpacesMessage(List<com.webapp.domain.Space> spacesByUserId) {
    }

    public static class Space{
        private final long id;
        private final String name;
        private final String description;
        private final Time createdTime;
        private final Time modifiedTime;

        public Space(long id, String name, String description, Time createdTime, Time modifiedTime) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.createdTime = createdTime;
            this.modifiedTime = modifiedTime;
        }
    }



}
