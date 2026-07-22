package com.unamba.apilibrary.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unamba.apilibrary.dto.response.ResponseSchoolGetAll;
import com.unamba.apilibrary.entity.EntityProfessionalSchool;
import com.unamba.apilibrary.repository.RepositoryProfessionalSchool;

@Service
public class BusinessSchool {
    private final RepositoryProfessionalSchool repositorySchool;

    public BusinessSchool(RepositoryProfessionalSchool repositorySchool) {
        this.repositorySchool = repositorySchool;
    }

    public ResponseSchoolGetAll getAll() {
        ResponseSchoolGetAll response = new ResponseSchoolGetAll();

        List<EntityProfessionalSchool> list = repositorySchool.findAllActive();

        list.forEach(s -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idSchool", s.getIdSchool());
            map.put("name", s.getName());
            map.put("faculty", s.getFaculty());
            map.put("abbreviation", s.getAbbreviation());
            response.getListSchool().add(map);
        });

        response.success();
        return response;
    }
}
