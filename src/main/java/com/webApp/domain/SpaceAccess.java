package com.webApp.testPart.domain;

import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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