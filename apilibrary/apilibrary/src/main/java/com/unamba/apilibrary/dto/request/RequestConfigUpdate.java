package com.unamba.apilibrary.dto.request;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestConfigUpdate {

    @NotNull(message = "Los parámetros de configuración son obligatorios.")
    private Map<String, String> configs;
}
