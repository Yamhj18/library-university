package com.unamba.apilibrary.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestBookInsert {
    @NotBlank(message = "The field \"idCategory\" is required.")
    private String idCategory;

    @NotBlank(message = "The field \"code\" is required.")
    private String code;

    @NotBlank(message = "The field \"title\" is required.")
    private String title;

    @NotBlank(message = "The field \"author\" is required.")
    private String author;

    @NotNull(message = "The field \"stock\" is required.")
    @Min(value = 1, message = "The field \"stock\" must be at least 1.")
    private Integer stock;

    private MultipartFile image;
}