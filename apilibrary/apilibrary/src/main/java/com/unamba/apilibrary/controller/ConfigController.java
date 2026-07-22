package com.unamba.apilibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.apilibrary.business.BusinessConfig;
import com.unamba.apilibrary.dto.request.RequestConfigUpdate;
import com.unamba.apilibrary.dto.response.ResponseConfigGetAll;
import com.unamba.apilibrary.dto.response.ResponseGenericMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "config")
public class ConfigController {

    private final BusinessConfig businessConfig;

    public ConfigController(BusinessConfig businessConfig) {
        this.businessConfig = businessConfig;
    }

    @GetMapping(path = "getall")
    public ResponseEntity<ResponseConfigGetAll> getAll() {
        try {
            return ResponseEntity.ok(businessConfig.getAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(path = "update")
    public ResponseEntity<ResponseGenericMessage> update(
            @Valid @RequestBody RequestConfigUpdate request,
            BindingResult bindingResult) {
        try {
            ResponseGenericMessage response;
            if (bindingResult.hasErrors()) {
                response = new ResponseGenericMessage();
                bindingResult.getAllErrors().forEach(e -> response.listMessage.add(e.getDefaultMessage()));
                return ResponseEntity.ok(response);
            }
            response = businessConfig.update(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
