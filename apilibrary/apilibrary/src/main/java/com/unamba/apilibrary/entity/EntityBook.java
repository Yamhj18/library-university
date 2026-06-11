package com.unamba.apilibrary.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbook")
@Getter
@Setter
public class EntityBook {
    @Id
    @Column(name = "idBook")
    private String idBook;

    @Column(name = "idCategory")
    private String idCategory;

    @Column(name = "code")
    private String code;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "imageName")
    private String imageName;

    @Column(name = "imageExtension")
    private String imageExtension;

    @Column(name = "status")
    private String status;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCategory", insertable = false, updatable = false)
    private EntityCategory parentCategory;

    @OneToMany(mappedBy = "parentBook", cascade = CascadeType.ALL)
    private List<EntityLoan> childLoan;
}