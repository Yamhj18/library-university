package com.unamba.apilibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.apilibrary.business.BusinessFaculty;
import com.unamba.apilibrary.dto.response.ResponseFacultyGetAll;

@RestController
@RequestMapping(path = "faculty")
public class FacultyController {
    private final BusinessFaculty businessFaculty;

    public FacultyController(BusinessFaculty businessFaculty) {
        this.businessFaculty = businessFaculty;
    }

    @GetMapping(path = "getall")
    public ResponseEntity<ResponseFacultyGetAll> getAll() {
        try {
            return ResponseEntity.ok(businessFaculty.getAll());
        } catch (Exception e) {
            return null;
        }
    }
}