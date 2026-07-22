package com.unamba.apilibrary.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAuthLogin {
    @NotBlank(message = "El correo institucional es obligatorio.")
    private String email;

    @NotBlank(message = "El DNI es obligatorio.")
    private String password;
}