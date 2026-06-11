package com.unamba.apilibrary.dto.response;

import com.unamba.apilibrary.generic.ResponseGeneric;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAuthLogin extends ResponseGeneric {
    private String token;
    private String role;
    private String fullName;
}