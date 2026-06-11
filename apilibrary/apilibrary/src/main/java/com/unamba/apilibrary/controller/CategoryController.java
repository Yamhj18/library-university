package com.unamba.apilibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.apilibrary.business.BusinessCategory;
import com.unamba.apilibrary.dto.response.ResponseCategoryGetAll;

@RestController
@RequestMapping(path = "category")
public class CategoryController {
    private final BusinessCategory businessCategory;

    public CategoryController(BusinessCategory businessCategory) {
        this.businessCategory = businessCategory;
    }

    @GetMapping(path = "getall")
    public ResponseEntity<ResponseCategoryGetAll> getAll() {
        try {
            return ResponseEntity.ok(businessCategory.getAll());
        } catch (Exception e) {
            return null;
        }
    }
}