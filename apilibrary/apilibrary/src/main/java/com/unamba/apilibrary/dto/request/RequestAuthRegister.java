package com.unamba.apilibrary.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAuthRegister {
    @NotBlank(message = "The field \"fullName\" is required.")
    private String fullName;

    @NotBlank(message = "The field \"email\" is required.")
    @Email(message = "The field \"email\" must be a valid email.")
    private String email;

    @NotBlank(message = "The field \"password\" is required.")
    @Size(min = 6, message = "The field \"password\" must be at least 6 characters.")
    private String password;
}