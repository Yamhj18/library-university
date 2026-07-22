package com.unamba.apilibrary.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLoanInsert {
    
    @NotBlank(message = "El libro es obligatorio.")
    private String idBook;

    @NotBlank(message = "El estudiante es obligatorio.")
    private String idUser;
    
    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "Debe prestar al menos 1 ejemplar.")
    private Integer quantity;

    @NotBlank(message = "El tipo de garantía es obligatorio.")
    private String guaranteeType;

    @NotBlank(message = "El número de garantía es obligatorio.")
    private String guaranteeNumber;

    @NotBlank(message = "La fecha estimada de devolución es obligatoria.")
    private String estimatedReturnDate;
    
    private String observations;
}