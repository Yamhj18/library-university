package com.unamba.apilibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.apilibrary.business.BusinessLoan;
import com.unamba.apilibrary.dto.request.RequestLoanInsert;
import com.unamba.apilibrary.dto.response.ResponseLoanGetAll;
import com.unamba.apilibrary.dto.response.ResponseLoanInsert;
import com.unamba.apilibrary.dto.response.ResponseLoanReturn;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "loan")
public class LoanController {
    private final BusinessLoan businessLoan;

    public LoanController(BusinessLoan businessLoan) {
        this.businessLoan = businessLoan;
    }

    @GetMapping(path = "getall")
    public ResponseEntity<ResponseLoanGetAll> getAll(
            @RequestParam(required = false) String search) {
        try {
            return ResponseEntity.ok(businessLoan.getAll(search));
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping(path = "insert")
    public ResponseEntity<ResponseLoanInsert> insert(
            @Valid @ModelAttribute RequestLoanInsert request,
            BindingResult bindingResult) {
        try {
            ResponseLoanInsert response;

            if (bindingResult.hasErrors()) {
                response = new ResponseLoanInsert();
                bindingResult.getAllErrors().forEach(error -> {
                    response.listMessage.add(error.getDefaultMessage());
                });
                return ResponseEntity.ok(response);
            }

            response = businessLoan.insert(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return null;
        }
    }

    @PutMapping(path = "return/{idLoan}")
    public ResponseEntity<ResponseLoanReturn> returnBook(
            @PathVariable String idLoan) {
        try {
            return ResponseEntity.ok(businessLoan.returnBook(idLoan));
        } catch (Exception e) {
            return null;
        }
    }
}