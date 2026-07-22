package com.unamba.apilibrary.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tprofessional_school")
@Getter
@Setter
public class EntityProfessionalSchool {

    @Id
    @Column(name = "id_school")
    private String idSchool;

    @Column(name = "name")
    private String name;

    @Column(name = "faculty")
    private String faculty;

    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "parentSchool", cascade = CascadeType.ALL)
    private List<EntityUser> childUsers;
}
