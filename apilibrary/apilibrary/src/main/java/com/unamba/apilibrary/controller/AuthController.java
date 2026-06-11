package com.unamba.apilibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    public AuthController(BusinessAuth businessAuth) {
        this.businessAuth = businessAuth;
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
            return null;
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
            return null;
        }
    }

    @GetMapping(path = "generatehash")
    public ResponseEntity<String> generateHash() {
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String hash = encoder.encode("password123");
        return ResponseEntity.ok(hash);
    }

}