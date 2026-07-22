package com.unamba.apilibrary.business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.unamba.apilibrary.dto.request.RequestStudentUpdate;
import com.unamba.apilibrary.dto.response.ResponseGenericMessage;
import com.unamba.apilibrary.dto.response.ResponseStudentGetAll;
import com.unamba.apilibrary.dto.response.ResponseStudentProfile;
import com.unamba.apilibrary.dto.response.StudentDto;
import com.unamba.apilibrary.dto.response.LoanDto;
import com.unamba.apilibrary.entity.EntityLoan;
import com.unamba.apilibrary.entity.EntityUser;
import com.unamba.apilibrary.repository.RepositoryLoan;
import com.unamba.apilibrary.repository.RepositoryUser;
import com.unamba.apilibrary.staticdata.EnumUserStatus;

@Service
public class BusinessStudent {

    private final RepositoryUser repositoryUser;
    private final RepositoryLoan repositoryLoan;

    public BusinessStudent(RepositoryUser repositoryUser, RepositoryLoan repositoryLoan) {
        this.repositoryUser = repositoryUser;
        this.repositoryLoan = repositoryLoan;
    }

    public ResponseStudentGetAll getAll(String search, String idSchool) {
        ResponseStudentGetAll response = new ResponseStudentGetAll();

        String searchParam = (search == null || search.isBlank()) ? null : search;
        String schoolParam = (idSchool == null || idSchool.isBlank()) ? null : idSchool;

        List<EntityUser> list = repositoryUser.findStudentsBySearchAndSchool(searchParam, schoolParam);

        list.forEach(u -> {
            StudentDto dto = mapStudent(u);
            dto.setActiveLoans(repositoryUser.countActiveLoans(u.getIdUser()));
            dto.setInactiveLoans(repositoryUser.countInactiveLoans(u.getIdUser()));
            response.getListStudent().add(dto);
        });

        response.success();
        return response;
    }

    public ResponseStudentGetAll search(String query) {
        ResponseStudentGetAll response = new ResponseStudentGetAll();

        if (query == null || query.isBlank()) {
            response.success();
            return response;
        }

        List<EntityUser> list = repositoryUser.searchStudents(query);
        list.forEach(u -> response.getListStudent().add(mapStudent(u)));

        response.success();
        return response;
    }

    public ResponseStudentProfile getProfile(String idUser) {
        ResponseStudentProfile response = new ResponseStudentProfile();

        EntityUser user = repositoryUser.findById(idUser).orElse(null);
        if (user == null) {
            response.listMessage.add("Estudiante no encontrado.");
            return response;
        }

        response.setStudent(mapStudent(user));

        // Active loans
        List<EntityLoan> activeLoans = repositoryLoan.findActiveByIdUser(idUser);
        activeLoans.forEach(l -> response.getActiveLoans().add(mapLoan(l)));

        // Loan history
        List<EntityLoan> allLoans = repositoryLoan.findByIdUser(idUser);
        allLoans.forEach(l -> response.getLoanHistory().add(mapLoan(l)));

        // Statistics
        Map<String, Object> stats = new HashMap<>();
        long totalLoans = allLoans.size();
        long active = activeLoans.size();
        long returned = allLoans.stream()
                .filter(l -> "Returned".equals(l.getStatus()))
                .count();

        stats.put("totalLoans", totalLoans);
        stats.put("activeLoans", active);
        stats.put("returnedLoans", returned);
        response.setStatistics(stats);

        response.success();
        return response;
    }

    public ResponseGenericMessage update(String idUser, RequestStudentUpdate request) {
        ResponseGenericMessage response = new ResponseGenericMessage();

        EntityUser user = repositoryUser.findById(idUser).orElse(null);
        if (user == null) {
            response.listMessage.add("Estudiante no encontrado.");
            return response;
        }

        // Check unique constraints
        if (!user.getEmail().equals(request.getEmail()) &&
                repositoryUser.existsByEmail(request.getEmail())) {
            response.listMessage.add("El correo ya está registrado por otro usuario.");
            return response;
        }

        if (!user.getDni().equals(request.getDni()) &&
                repositoryUser.existsByDni(request.getDni())) {
            response.listMessage.add("El DNI ya está registrado por otro usuario.");
            return response;
        }

        if (!user.getStudentCode().equals(request.getStudentCode()) &&
                repositoryUser.existsByStudentCode(request.getStudentCode())) {
            response.listMessage.add("El código de estudiante ya está registrado.");
            return response;
        }

        user.setFullName(request.getFullName());
        user.setDni(request.getDni());
        user.setStudentCode(request.getStudentCode());
        user.setIdSchool(request.getIdSchool());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUpdatedAt(new Date());

        repositoryUser.save(user);

        response.success();
        response.listMessage.add("Estudiante actualizado exitosamente.");
        return response;
    }

    public ResponseGenericMessage delete(String idUser) {
        ResponseGenericMessage response = new ResponseGenericMessage();

        EntityUser user = repositoryUser.findById(idUser).orElse(null);
        if (user == null) {
            response.listMessage.add("Estudiante no encontrado.");
            return response;
        }

        // Check if student has active loans
        long activeLoans = repositoryUser.countActiveLoans(idUser);
        if (activeLoans > 0) {
            response.listMessage.add("No se puede eliminar un estudiante con préstamos activos.");
            return response;
        }

        user.setStatus(EnumUserStatus.INACTIVE.toString());
        user.setUpdatedAt(new Date());
        repositoryUser.save(user);

        response.success();
        response.listMessage.add("Estudiante eliminado exitosamente.");
        return response;
    }

    private StudentDto mapStudent(EntityUser u) {
        StudentDto dto = new StudentDto();
        dto.setIdUser(u.getIdUser());
        dto.setFullName(u.getFullName());
        dto.setDni(u.getDni());
        dto.setEmail(u.getEmail());
        dto.setStudentCode(u.getStudentCode());
        dto.setPhoneNumber(u.getPhoneNumber());
        dto.setIdSchool(u.getIdSchool());
        dto.setProfileImage(u.getProfileImage());
        dto.setStatus(u.getStatus());

        if (u.getParentSchool() != null) {
            dto.setSchoolName(u.getParentSchool().getName());
        }

        return dto;
    }

    private LoanDto mapLoan(EntityLoan l) {
        LoanDto dto = new LoanDto();
        dto.setIdLoan(l.getIdLoan());
        dto.setCode(l.getCode());
        dto.setLoanDate(l.getLoanDate());
        dto.setEstimatedReturnDate(l.getEstimatedReturnDate());
        dto.setActualReturnDate(l.getActualReturnDate());
        dto.setStatus(l.getStatus());
        dto.setObservations(l.getObservations());
        dto.setReturnObservations(l.getReturnObservations());

        if (l.getParentBook() != null) {
            dto.setBookTitle(l.getParentBook().getTitle());
            dto.setBookCode(l.getParentBook().getCode());
            dto.setBookAuthor(l.getParentBook().getAuthor());
        }

        return dto;
    }
}
