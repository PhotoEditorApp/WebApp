package com.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "space_access")
public class SpaceAccess implements Serializable {
    @Id
    private Long userId;
    @Id
    private Long spaceId;
    @Id
    private Long access_statusId;
}