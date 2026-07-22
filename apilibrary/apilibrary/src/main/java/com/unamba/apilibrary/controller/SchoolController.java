package com.unamba.apilibrary.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.apilibrary.dto.response.ResponseSchoolGetAll;
import com.unamba.apilibrary.entity.EntityProfessionalSchool;
import com.unamba.apilibrary.repository.RepositoryProfessionalSchool;

@RestController
@RequestMapping(path = "school")
public class SchoolController {

    private final RepositoryProfessionalSchool repositorySchool;

    public SchoolController(RepositoryProfessionalSchool repositorySchool) {
        this.repositorySchool = repositorySchool;
    }

    @GetMapping(path = "getall")
    public ResponseEntity<ResponseSchoolGetAll> getAll() {
        try {
            ResponseSchoolGetAll response = new ResponseSchoolGetAll();

            List<EntityProfessionalSchool> list = repositorySchool.findByStatus("Active");

            list.forEach(s -> {
                Map<String, Object> map = new HashMap<>();
                map.put("idSchool", s.getIdSchool());
                map.put("name", s.getName());
                map.put("faculty", s.getFaculty());
                map.put("abbreviation", s.getAbbreviation());
                response.getListSchool().add(map);
            });

            response.success();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
