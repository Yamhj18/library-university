package com.unamba.apilibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unamba.apilibrary.business.BusinessLoan;
import com.unamba.apilibrary.dto.request.RequestLoanInsert;
import com.unamba.apilibrary.dto.request.RequestLoanReturn;
import com.unamba.apilibrary.dto.response.ResponseLoanGetAll;
import com.unamba.apilibrary.dto.response.ResponseLoanHistory;
import com.unamba.apilibrary.dto.response.ResponseLoanInsert;
import com.unamba.apilibrary.dto.response.ResponseLoanReturn;
import com.unamba.apilibrary.helper.JwtHelper;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "loan")
public class LoanController {
    private final BusinessLoan businessLoan;
    private final JwtHelper jwtHelper;

    public LoanController(BusinessLoan businessLoan, JwtHelper jwtHelper) {
        this.businessLoan = businessLoan;
        this.jwtHelper = jwtHelper;
    }

    @GetMapping(path = "getall")
    public ResponseEntity<ResponseLoanGetAll> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String idSchool,
            @RequestParam(required = false) String status) {
        try {
            return ResponseEntity.ok(businessLoan.getAll(search, idSchool, status));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "history")
    public ResponseEntity<ResponseLoanHistory> getHistory(
            @RequestParam(required = false) String search) {
        try {
            return ResponseEntity.ok(businessLoan.getHistory(search));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "student/{idUser}")
    public ResponseEntity<ResponseLoanHistory> getByStudent(
            @PathVariable String idUser) {
        try {
            return ResponseEntity.ok(businessLoan.getByStudent(idUser));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "my-loans")
    public ResponseEntity<ResponseLoanHistory> getMyLoans(
            jakarta.servlet.http.HttpServletRequest httpRequest) {
        try {
            String token = httpRequest.getHeader("Authorization").substring(7);
            String idUser = jwtHelper.extractClaims(token).get("idUser", String.class);
            return ResponseEntity.ok(businessLoan.getMyLoans(idUser));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "insert")
    public ResponseEntity<ResponseLoanInsert> insert(
            @Valid @ModelAttribute RequestLoanInsert request,
            BindingResult bindingResult,
            jakarta.servlet.http.HttpServletRequest httpRequest) {
        try {
            ResponseLoanInsert response;

            if (bindingResult.hasErrors()) {
                response = new ResponseLoanInsert();
                bindingResult.getAllErrors().forEach(error -> {
                    response.listMessage.add(error.getDefaultMessage());
                });
                return ResponseEntity.ok(response);
            }

            String token = httpRequest.getHeader("Authorization").substring(7);
            String idAdmin = jwtHelper.extractClaims(token).get("idUser", String.class);

            response = businessLoan.insert(request, idAdmin);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(path = "return/{idLoan}")
    public ResponseEntity<ResponseLoanReturn> returnBook(
            @PathVariable String idLoan,
            @RequestBody(required = false) RequestLoanReturn request) {
        try {
            return ResponseEntity.ok(businessLoan.returnBook(idLoan, request));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}