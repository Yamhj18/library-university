package com.unamba.apilibrary.business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.unamba.apilibrary.dto.request.RequestLoanInsert;
import com.unamba.apilibrary.dto.response.ResponseLoanGetAll;
import com.unamba.apilibrary.dto.response.ResponseLoanInsert;
import com.unamba.apilibrary.dto.response.ResponseLoanReturn;
import com.unamba.apilibrary.entity.EntityBook;
import com.unamba.apilibrary.entity.EntityLoan;
import com.unamba.apilibrary.helper.GenericHelper;
import com.unamba.apilibrary.repository.RepositoryBook;
import com.unamba.apilibrary.repository.RepositoryLoan;
import com.unamba.apilibrary.staticdata.EnumBookStatus;
import com.unamba.apilibrary.staticdata.EnumLoanStatus;

@Service
public class BusinessLoan {
    private final RepositoryLoan repositoryLoan;
    private final RepositoryBook repositoryBook;

    public BusinessLoan(
            RepositoryLoan repositoryLoan,
            RepositoryBook repositoryBook) {
        this.repositoryLoan = repositoryLoan;
        this.repositoryBook = repositoryBook;
    }

    public ResponseLoanGetAll getAll(String search) {
        ResponseLoanGetAll response = new ResponseLoanGetAll();

        String searchParam = (search == null || search.isBlank()) ? null : search;

        List<EntityLoan> listActive = repositoryLoan.findActiveBySearch(searchParam);
        List<EntityLoan> listOverdue = repositoryLoan.findOverdue();

        listActive.forEach(l -> response.getListLoan().add(mapLoan(l)));
        listOverdue.forEach(l -> response.getListOverdue().add(mapLoan(l)));

        response.success();
        return response;
    }

    public ResponseLoanInsert insert(RequestLoanInsert request) {
        ResponseLoanInsert response = new ResponseLoanInsert();

        EntityBook book = repositoryBook.findById(request.getIdBook()).orElse(null);

        if (book == null) {
            response.listMessage.add("Book not found.");
            return response;
        }

        if (book.getStock() <= 0) {
            response.listMessage.add("The book is not available.");
            return response;
        }

        EntityLoan entity = new EntityLoan();
        entity.setIdLoan(UUID.randomUUID().toString());
        entity.setIdBook(request.getIdBook());
        entity.setCode(GenericHelper.generateCode());
        entity.setStudentCode(request.getStudentCode());
        entity.setStudentName(request.getStudentName());
        entity.setFaculty(request.getFaculty());
        entity.setPhoneNumber(request.getPhoneNumber());
        entity.setGuaranteeType(request.getGuaranteeType());
        entity.setGuaranteeNumber(request.getGuaranteeNumber());
        entity.setLoanDate(new java.sql.Date(new Date().getTime()));
        entity.setEstimatedReturnDate(java.sql.Date.valueOf(request.getEstimatedReturnDate()));
        entity.setStatus(EnumLoanStatus.ACTIVE.toString());
        entity.setCreatedAt(new java.sql.Date(new Date().getTime()));
        entity.setUpdatedAt(entity.getCreatedAt());

        repositoryLoan.save(entity);

        // Update book stock
        book.setStock(book.getStock() - 1);
        if (book.getStock() == 0) {
            book.setStatus(EnumBookStatus.UNAVAILABLE.toString());
        }
        book.setUpdatedAt(new java.sql.Date(new Date().getTime()));
        repositoryBook.save(book);

        response.success();
        response.listMessage.add("Loan registered successfully.");
        return response;
    }

    public ResponseLoanReturn returnBook(String idLoan) {
        ResponseLoanReturn response = new ResponseLoanReturn();

        EntityLoan loan = repositoryLoan.findById(idLoan).orElse(null);

        if (loan == null) {
            response.listMessage.add("Loan not found.");
            return response;
        }

        if (loan.getStatus().equals(EnumLoanStatus.RETURNED.toString())) {
            response.listMessage.add("This loan has already been returned.");
            return response;
        }

        loan.setActualReturnDate(new java.sql.Date(new Date().getTime()));
        loan.setStatus(EnumLoanStatus.RETURNED.toString());
        loan.setUpdatedAt(new java.sql.Date(new Date().getTime()));
        repositoryLoan.save(loan);

        // Update book stock
        EntityBook book = repositoryBook.findById(loan.getIdBook()).orElse(null);
        if (book != null) {
            book.setStock(book.getStock() + 1);
            book.setStatus(EnumBookStatus.AVAILABLE.toString());
            book.setUpdatedAt(new java.sql.Date(new Date().getTime()));
            repositoryBook.save(book);
        }

        response.success();
        response.listMessage.add("Book returned successfully.");
        return response;
    }

    private Map<String, Object> mapLoan(EntityLoan l) {
        Map<String, Object> map = new HashMap<>();
        map.put("idLoan", l.getIdLoan());
        map.put("code", l.getCode());
        map.put("studentCode", l.getStudentCode());
        map.put("studentName", l.getStudentName());
        map.put("faculty", l.getFaculty());
        map.put("phoneNumber", l.getPhoneNumber());
        map.put("guaranteeType", l.getGuaranteeType());
        map.put("guaranteeNumber", l.getGuaranteeNumber());
        map.put("loanDate", l.getLoanDate());
        map.put("estimatedReturnDate", l.getEstimatedReturnDate());
        map.put("status", l.getStatus());
        map.put("idBook", l.getIdBook());

        if (l.getParentBook() != null) {
            map.put("bookTitle", l.getParentBook().getTitle());
            map.put("bookCode", l.getParentBook().getCode());
            map.put("bookAuthor", l.getParentBook().getAuthor());
        }

        return map;
    }
}