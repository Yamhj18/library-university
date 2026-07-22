package com.unamba.apilibrary.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.apilibrary.business.BusinessBook;
import com.unamba.apilibrary.dto.request.RequestBookInsert;
import com.unamba.apilibrary.dto.request.RequestBookUpdate;
import com.unamba.apilibrary.dto.response.ResponseBookGetAll;
import com.unamba.apilibrary.dto.response.ResponseBookInsert;
import com.unamba.apilibrary.dto.response.ResponseGenericMessage;

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
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "get/{idBook}")
    public ResponseEntity<ResponseBookGetAll> getById(
            @PathVariable String idBook) {
        try {
            return ResponseEntity.ok(businessBook.getById(idBook));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
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
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(path = "update/{idBook}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseGenericMessage> update(
            @PathVariable String idBook,
            @Valid @ModelAttribute RequestBookUpdate request,
            BindingResult bindingResult) {
        try {
            ResponseGenericMessage response;

            if (bindingResult.hasErrors()) {
                response = new ResponseGenericMessage();
                bindingResult.getAllErrors().forEach(error -> {
                    response.listMessage.add(error.getDefaultMessage());
                });
                return ResponseEntity.ok(response);
            }

            response = businessBook.update(idBook, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(path = "delete/{idBook}")
    public ResponseEntity<ResponseGenericMessage> delete(
            @PathVariable String idBook) {
        try {
            return ResponseEntity.ok(businessBook.delete(idBook));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}