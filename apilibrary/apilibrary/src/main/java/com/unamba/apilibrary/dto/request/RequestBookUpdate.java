package com.unamba.apilibrary.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestBookUpdate {

    @NotBlank(message = "El código es obligatorio.")
    private String code;

    @NotBlank(message = "El título es obligatorio.")
    private String title;

    @NotBlank(message = "El autor es obligatorio.")
    private String author;

    private String description;

    private Integer publicationYear;

    @NotNull(message = "El stock total es obligatorio.")
    @Min(value = 1, message = "El stock total debe ser al menos 1.")
    private Integer stockTotal;

    @NotBlank(message = "La categoría es obligatoria.")
    private String idCategory;

    private MultipartFile image;
}
