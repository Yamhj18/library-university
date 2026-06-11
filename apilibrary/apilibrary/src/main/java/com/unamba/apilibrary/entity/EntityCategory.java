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
@Table(name = "tcategory")
@Getter
@Setter
public class EntityCategory {
    @Id
    @Column(name = "idCategory")
    private String idCategory;

    @Column(name = "name")
    private String name;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    private List<EntityBook> childBook;
}