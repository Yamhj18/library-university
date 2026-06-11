package com.unamba.apilibrary.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestAuthLogin {
    @NotBlank(message = "The field \"email\" is required.")
    private String email;

    @NotBlank(message = "The field \"password\" is required.")
    private String password;
}