package com.unamba.apilibrary.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tloan")
@Getter
@Setter
public class EntityLoan {
    @Id
    @Column(name = "idLoan")
    private String idLoan;

    @Column(name = "idBook")
    private String idBook;

    @Column(name = "code")
    private String code;

    @Column(name = "studentCode")
    private String studentCode;

    @Column(name = "studentName")
    private String studentName;

    @Column(name = "faculty")
    private String faculty;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "guaranteeType")
    private String guaranteeType;

    @Column(name = "guaranteeNumber")
    private String guaranteeNumber;

    @Column(name = "loanDate")
    private Date loanDate;

    @Column(name = "estimatedReturnDate")
    private Date estimatedReturnDate;

    @Column(name = "actualReturnDate")
    private Date actualReturnDate;

    @Column(name = "status")
    private String status;

    @Column(name = "createdAt")
    private Date createdAt;

    @Column(name = "updatedAt")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idBook", insertable = false, updatable = false)
    private EntityBook parentBook;
}