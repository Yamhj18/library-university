package com.unamba.apilibrary.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLoanInsert {
    @NotBlank(message = "The field \"idBook\" is required.")
    private String idBook;

    @NotBlank(message = "The field \"studentCode\" is required.")
    private String studentCode;

    @NotBlank(message = "The field \"studentName\" is required.")
    private String studentName;

    @NotBlank(message = "The field \"faculty\" is required.")
    private String faculty;

    @NotBlank(message = "The field \"phoneNumber\" is required.")
    private String phoneNumber;

    @NotBlank(message = "The field \"guaranteeType\" is required.")
    private String guaranteeType;

    @NotBlank(message = "The field \"guaranteeNumber\" is required.")
    private String guaranteeNumber;

    @NotBlank(message = "The field \"estimatedReturnDate\" is required.")
    private String estimatedReturnDate;
}