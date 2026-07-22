package com.unamba.apilibrary.business;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unamba.apilibrary.dto.request.RequestLoanInsert;
import com.unamba.apilibrary.dto.request.RequestLoanReturn;
import com.unamba.apilibrary.dto.response.ResponseLoanGetAll;
import com.unamba.apilibrary.dto.response.ResponseLoanHistory;
import com.unamba.apilibrary.dto.response.ResponseLoanInsert;
import com.unamba.apilibrary.dto.response.ResponseLoanReturn;
import com.unamba.apilibrary.dto.response.LoanDto;
import com.unamba.apilibrary.entity.EntityBook;
import com.unamba.apilibrary.entity.EntityLoan;
import com.unamba.apilibrary.helper.GenericHelper;
import com.unamba.apilibrary.repository.RepositoryBook;
import com.unamba.apilibrary.repository.RepositoryLoan;
import com.unamba.apilibrary.staticdata.EnumBookStatus;
import com.unamba.apilibrary.staticdata.EnumLoanStatus;

@Service
@Transactional
public class BusinessLoan {
    private final RepositoryLoan repositoryLoan;
    private final RepositoryBook repositoryBook;

    public BusinessLoan(
            RepositoryLoan repositoryLoan,
            RepositoryBook repositoryBook) {
        this.repositoryLoan = repositoryLoan;
        this.repositoryBook = repositoryBook;
    }

    public ResponseLoanGetAll getAll(String search, String idSchool, String status) {
        ResponseLoanGetAll response = new ResponseLoanGetAll();

        String searchParam = (search == null || search.isBlank()) ? null : search;
        String schoolParam = (idSchool == null || idSchool.isBlank()) ? null : idSchool;
        String statusParam = (status == null || status.isBlank()) ? null : status;

        List<EntityLoan> listActive = repositoryLoan.findActiveBySearchAndFilters(searchParam, schoolParam,
                statusParam);
        List<EntityLoan> listOverdue = repositoryLoan.findOverdue();

        listActive.forEach(l -> response.getListLoan().add(mapLoan(l)));
        listOverdue.forEach(l -> response.getListOverdue().add(mapLoan(l)));

        response.success();
        return response;
    }

    public ResponseLoanHistory getHistory(String search) {
        ResponseLoanHistory response = new ResponseLoanHistory();

        List<EntityLoan> list = repositoryLoan.findAllHistory();
        list.forEach(l -> response.getListLoan().add(mapLoan(l)));

        response.success();
        return response;
    }

    public ResponseLoanHistory getMyLoans(String idUser) {
        ResponseLoanHistory response = new ResponseLoanHistory();

        List<EntityLoan> list = repositoryLoan.findByIdUser(idUser);
        list.forEach(l -> response.getListLoan().add(mapLoan(l)));

        response.success();
        return response;
    }

    public ResponseLoanHistory getByStudent(String idUser) {
        ResponseLoanHistory response = new ResponseLoanHistory();

        List<EntityLoan> list = repositoryLoan.findByIdUser(idUser);
        list.forEach(l -> response.getListLoan().add(mapLoan(l)));

        response.success();
        return response;
    }

    public ResponseLoanInsert insert(RequestLoanInsert request, String idAdmin) {
        ResponseLoanInsert response = new ResponseLoanInsert();

        EntityBook book = repositoryBook.findById(request.getIdBook()).orElse(null);

        if (book == null) {
            response.listMessage.add("El libro no fue encontrado.");
            return response;
        }

        if (book.getStockAvailable() < request.getQuantity()) {
            response.listMessage.add("No hay suficientes ejemplares disponibles.");
            return response;
        }

        EntityLoan entity = new EntityLoan();
        entity.setIdLoan(UUID.randomUUID().toString());
        entity.setIdBook(request.getIdBook());
        entity.setIdUser(request.getIdUser());
        entity.setRegisteredBy(idAdmin);
        entity.setQuantity(request.getQuantity());
        entity.setCode(GenericHelper.generateCode());
        entity.setGuaranteeType(request.getGuaranteeType());
        entity.setGuaranteeNumber(request.getGuaranteeNumber());
        entity.setLoanDate(LocalDate.now());
        entity.setEstimatedReturnDate(LocalDate.parse(request.getEstimatedReturnDate()));
        entity.setObservations(request.getObservations());
        entity.setStatus(EnumLoanStatus.ACTIVE.toString());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(entity.getCreatedAt());

        repositoryLoan.save(entity);

        // Update book stock available
        book.setStockAvailable(book.getStockAvailable() - request.getQuantity());
        if (book.getStockAvailable() == 0) {
            book.setStatus(EnumBookStatus.UNAVAILABLE.toString());
        }
        book.setUpdatedAt(new java.sql.Date(System.currentTimeMillis()));
        repositoryBook.save(book);

        response.success();
        response.listMessage.add("Préstamo registrado exitosamente.");
        return response;
    }

    public ResponseLoanReturn returnBook(String idLoan, RequestLoanReturn request) {
        ResponseLoanReturn response = new ResponseLoanReturn();

        EntityLoan loan = repositoryLoan.findById(idLoan).orElse(null);

        if (loan == null) {
            response.listMessage.add("Préstamo no encontrado.");
            return response;
        }

        if (loan.getStatus().equals(EnumLoanStatus.RETURNED.toString())) {
            response.listMessage.add("Este préstamo ya fue devuelto.");
            return response;
        }

        loan.setActualReturnDate(LocalDate.now());
        loan.setStatus(EnumLoanStatus.RETURNED.toString());
        loan.setReturnObservations(request != null ? request.getObservations() : null);
        loan.setUpdatedAt(LocalDateTime.now());
        repositoryLoan.save(loan);
        repositoryLoan.save(loan);

        // Update book stock available
        EntityBook book = repositoryBook.findById(loan.getIdBook()).orElse(null);
        if (book != null) {
            book.setStockAvailable(book.getStockAvailable() + loan.getQuantity());
            book.setStatus(EnumBookStatus.AVAILABLE.toString());
            book.setUpdatedAt(new java.sql.Date(System.currentTimeMillis()));
            repositoryBook.save(book);
        }

        response.success();
        response.listMessage.add("Libro devuelto exitosamente.");
        return response;
    }

    private LoanDto mapLoan(EntityLoan l) {
        LoanDto dto = new LoanDto();
        dto.setIdLoan(l.getIdLoan());
        dto.setCode(l.getCode());
        dto.setQuantity(l.getQuantity());
        dto.setGuaranteeType(l.getGuaranteeType());
        dto.setGuaranteeNumber(l.getGuaranteeNumber());
        dto.setLoanDate(l.getLoanDate());
        dto.setEstimatedReturnDate(l.getEstimatedReturnDate());
        dto.setActualReturnDate(l.getActualReturnDate());
        dto.setObservations(l.getObservations());
        dto.setReturnObservations(l.getReturnObservations());
        dto.setStatus(l.getStatus());
        dto.setIdBook(l.getIdBook());
        dto.setIdUser(l.getIdUser());

        if (l.getParentBook() != null) {
            dto.setBookTitle(l.getParentBook().getTitle());
            dto.setBookCode(l.getParentBook().getCode());
            dto.setBookAuthor(l.getParentBook().getAuthor());
        }

        if (l.getParentUser() != null) {
            dto.setStudentName(l.getParentUser().getFullName());
            dto.setStudentCode(l.getParentUser().getStudentCode());
            dto.setStudentDni(l.getParentUser().getDni());

            if (l.getParentUser().getParentSchool() != null) {
                dto.setStudentSchool(l.getParentUser().getParentSchool().getName());
            }
        }

        if (l.getRegisteredByUser() != null) {
            dto.setRegisteredBy(l.getRegisteredByUser().getFullName());
        }

        return dto;
    }
}