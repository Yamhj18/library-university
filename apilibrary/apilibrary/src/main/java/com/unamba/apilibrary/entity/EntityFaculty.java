package com.unamba.apilibrary.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tfaculty")
@Getter
@Setter
public class EntityFaculty {
    @Id
    @Column(name = "idFaculty")
    private String idFaculty;

    @Column(name = "name")
    private String name;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;
}