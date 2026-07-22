package com.unamba.apilibrary.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    @Column(name = "id_loan")
    private String idLoan;

    @Column(name = "code")
    private String code;

    @Column(name = "id_book")
    private String idBook;

    @Column(name = "id_user")
    private String idUser;

    @Column(name = "registered_by")
    private String registeredBy;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "guarantee_type")
    private String guaranteeType;

    @Column(name = "guarantee_number")
    private String guaranteeNumber;

    @Column(name = "loan_date")
    private LocalDate loanDate;

    @Column(name = "estimated_return_date")
    private LocalDate estimatedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;

    @Column(name = "observations")
    private String observations;

    @Column(name = "return_observations")
    private String returnObservations;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_book", insertable = false, updatable = false)
    private EntityBook parentBook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", insertable = false, updatable = false)
    private EntityUser parentUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registered_by", insertable = false, updatable = false)
    private EntityUser registeredByUser;
}