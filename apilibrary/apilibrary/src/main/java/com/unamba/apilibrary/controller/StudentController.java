package com.unamba.apilibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.apilibrary.business.BusinessStudent;
import com.unamba.apilibrary.dto.request.RequestStudentUpdate;
import com.unamba.apilibrary.dto.response.ResponseGenericMessage;
import com.unamba.apilibrary.dto.response.ResponseStudentGetAll;
import com.unamba.apilibrary.dto.response.ResponseStudentProfile;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "student")
public class StudentController {

    private final BusinessStudent businessStudent;

    public StudentController(BusinessStudent businessStudent) {
        this.businessStudent = businessStudent;
    }

    @GetMapping(path = "getall")
    public ResponseEntity<ResponseStudentGetAll> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String idSchool) {
        try {
            return ResponseEntity.ok(businessStudent.getAll(search, idSchool));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "search")
    public ResponseEntity<ResponseStudentGetAll> search(
            @RequestParam(required = false) String q) {
        try {
            return ResponseEntity.ok(businessStudent.search(q));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "profile/{idUser}")
    public ResponseEntity<ResponseStudentProfile> getProfile(
            @PathVariable String idUser) {
        try {
            return ResponseEntity.ok(businessStudent.getProfile(idUser));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(path = "update/{idUser}")
    public ResponseEntity<ResponseGenericMessage> update(
            @PathVariable String idUser,
            @Valid @RequestBody RequestStudentUpdate request,
            BindingResult bindingResult) {
        try {
            ResponseGenericMessage response;
            if (bindingResult.hasErrors()) {
                response = new ResponseGenericMessage();
                bindingResult.getAllErrors().forEach(e -> response.listMessage.add(e.getDefaultMessage()));
                return ResponseEntity.ok(response);
            }
            response = businessStudent.update(idUser, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(path = "delete/{idUser}")
    public ResponseEntity<ResponseGenericMessage> delete(
            @PathVariable String idUser) {
        try {
            return ResponseEntity.ok(businessStudent.delete(idUser));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
