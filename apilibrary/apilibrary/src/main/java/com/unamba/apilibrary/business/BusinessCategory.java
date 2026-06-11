package com.unamba.apilibrary.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unamba.apilibrary.dto.response.ResponseCategoryGetAll;
import com.unamba.apilibrary.entity.EntityCategory;
import com.unamba.apilibrary.repository.RepositoryCategory;

@Service
public class BusinessCategory {
    private final RepositoryCategory repositoryCategory;

    public BusinessCategory(RepositoryCategory repositoryCategory) {
        this.repositoryCategory = repositoryCategory;
    }

    public ResponseCategoryGetAll getAll() {
        ResponseCategoryGetAll response = new ResponseCategoryGetAll();

        List<EntityCategory> list = repositoryCategory.findAll();

        list.forEach(c -> {
            Map<String, String> map = new HashMap<>();
            map.put("idCategory", c.getIdCategory());
            map.put("name", c.getName());
            response.getListCategory().add(map);
        });

        response.success();
        return response;
    }
}