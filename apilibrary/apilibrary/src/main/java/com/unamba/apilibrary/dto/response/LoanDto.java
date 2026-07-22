package com.unamba.apilibrary.dto.response;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanDto {
    private String idLoan;
    private String code;
    private Integer quantity;
    private String guaranteeType;
    private String guaranteeNumber;
    private Date loanDate;
    private Date estimatedReturnDate;
    private Date actualReturnDate;
    private String observations;
    private String returnObservations;
    private String status;
    private String idBook;
    private String idUser;
    
    // Nested/Related fields mapped flat
    private String bookTitle;
    private String bookCode;
    private String bookAuthor;
    private String studentName;
    private String studentCode;
    private String studentDni;
    private String studentSchool;
    private String registeredBy;
}
