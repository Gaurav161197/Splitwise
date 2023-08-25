package com.application.splitwise.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


@MappedSuperclass
@Data
@EntityListeners(value = AuditingEntityListener.class)
public class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
}


