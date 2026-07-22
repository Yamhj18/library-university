package com.unamba.apilibrary.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unamba.apilibrary.dto.response.ResponseFacultyGetAll;
import com.unamba.apilibrary.entity.EntityFaculty;
import com.unamba.apilibrary.repository.RepositoryFaculty;

@Service
public class BusinessFaculty {
    private final RepositoryFaculty repositoryFaculty;

    public BusinessFaculty(RepositoryFaculty repositoryFaculty) {
        this.repositoryFaculty = repositoryFaculty;
    }

    public ResponseFacultyGetAll getAll() {
        ResponseFacultyGetAll response = new ResponseFacultyGetAll();

        List<EntityFaculty> list = repositoryFaculty.findAll();

        list.forEach(f -> {
            Map<String, String> map = new HashMap<>();
            map.put("idFaculty", String.valueOf(f.getIdFaculty()));
            map.put("name", f.getName());
            response.getListFaculty().add(map);
        });

        response.success();
        return response;
    }
}