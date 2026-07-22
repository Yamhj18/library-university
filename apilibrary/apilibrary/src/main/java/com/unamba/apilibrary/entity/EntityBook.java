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
    @Column(name = "id_book")
    private String idBook;

    @Column(name = "id_category")
    private String idCategory;

    @Column(name = "code")
    private String code;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "description")
    private String description;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "stock_total")
    private Integer stockTotal;

    @Column(name = "stock_available")
    private Integer stockAvailable;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_extension")
    private String imageExtension;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", insertable = false, updatable = false)
    private EntityCategory parentCategory;

    @OneToMany(mappedBy = "parentBook", cascade = CascadeType.ALL)
    private List<EntityLoan> childLoans;
}