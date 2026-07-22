package com.unamba.apilibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.apilibrary.business.BusinessAuth;
import com.unamba.apilibrary.dto.request.RequestAuthLogin;
import com.unamba.apilibrary.dto.request.RequestAuthRegister;
import com.unamba.apilibrary.dto.response.ResponseAuthLogin;
import com.unamba.apilibrary.dto.response.ResponseAuthRegister;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "auth")
public class AuthController {

    private final BusinessAuth businessAuth;
    private final PasswordEncoder passwordEncoder;

    public AuthController(BusinessAuth businessAuth, PasswordEncoder passwordEncoder) {
        this.businessAuth = businessAuth;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "login")
    public ResponseEntity<ResponseAuthLogin> login(
            @Valid @RequestBody RequestAuthLogin request,
            BindingResult bindingResult) {
        try {
            ResponseAuthLogin response;
            if (bindingResult.hasErrors()) {
                response = new ResponseAuthLogin();
                bindingResult.getAllErrors().forEach(e -> response.listMessage.add(e.getDefaultMessage()));
                return ResponseEntity.ok(response);
            }
            response = businessAuth.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "register")
    public ResponseEntity<ResponseAuthRegister> register(
            @Valid @RequestBody RequestAuthRegister request,
            BindingResult bindingResult) {
        try {
            ResponseAuthRegister response;
            if (bindingResult.hasErrors()) {
                response = new ResponseAuthRegister();
                bindingResult.getAllErrors().forEach(e -> response.listMessage.add(e.getDefaultMessage()));
                return ResponseEntity.ok(response);
            }
            response = businessAuth.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "generatehash")
    public ResponseEntity<String> generateHash(
            @RequestParam(defaultValue = "00000001") String value) {
        String hash = passwordEncoder.encode(value);
        return ResponseEntity.ok(hash);
    }
}