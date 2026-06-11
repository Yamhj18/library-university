package com.unamba.apilibrary.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.apilibrary.business.BusinessBook;
import com.unamba.apilibrary.dto.request.RequestBookInsert;
import com.unamba.apilibrary.dto.response.ResponseBookGetAll;
import com.unamba.apilibrary.dto.response.ResponseBookInsert;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "book")
public class BookController {
    private final BusinessBook businessBook;

    public BookController(BusinessBook businessBook) {
        this.businessBook = businessBook;
    }

    @GetMapping(path = "getall")
    public ResponseEntity<ResponseBookGetAll> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String idCategory) {
        try {
            return ResponseEntity.ok(businessBook.getAll(search, idCategory));
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping(path = "insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseBookInsert> insert(
            @Valid @ModelAttribute RequestBookInsert request,
            BindingResult bindingResult) {
        try {
            ResponseBookInsert response;

            if (bindingResult.hasErrors()) {
                response = new ResponseBookInsert();
                bindingResult.getAllErrors().forEach(error -> {
                    response.listMessage.add(error.getDefaultMessage());
                });
                return ResponseEntity.ok(response);
            }

            response = businessBook.insert(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return null;
        }
    }
}