package com.unamba.apilibrary.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestStudentUpdate {

    @NotBlank(message = "El nombre completo es obligatorio.")
    private String fullName;

    @NotBlank(message = "El DNI es obligatorio.")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 dígitos.")
    private String dni;

    @NotBlank(message = "El código de estudiante es obligatorio.")
    private String studentCode;

    @NotBlank(message = "La escuela profesional es obligatoria.")
    private String idSchool;

    @NotBlank(message = "El correo institucional es obligatorio.")
    @Email(message = "Debe ser un correo electrónico válido.")
    private String email;

    private String phoneNumber;
}
