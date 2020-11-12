package com.webapp.json;

import com.webapp.enums.AccessType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;


public class SpaceAccessRequest implements Serializable {

     private Long user_id;
     private Long space_id;

     private AccessType type;

     public SpaceAccessRequest(){}

     public SpaceAccessRequest(Long user_id, Long space_id, AccessType type) {
          this.user_id = user_id;
          this.space_id = space_id;
          this.type = type;
     }

     public Long getUser_id() {
          return user_id;
     }

     public void setUser_id(Long user_id) {
          this.user_id = user_id;
     }

     public Long getSpace_id() {
          return space_id;
     }

     public void setSpace_id(Long space_id) {
          this.space_id = space_id;
     }

     public AccessType getType() {
          return type;
     }

     public void setType(AccessType type) {
          this.type = type;
     }
}
