package com.unamba.apilibrary.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unamba.apilibrary.entity.EntityUser;

@Repository
public interface RepositoryUser extends JpaRepository<EntityUser, String> {
    Optional<EntityUser> findByEmail(String email);

    Optional<EntityUser> findByDni(String dni);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    boolean existsByStudentCode(String studentCode);

    @Query("SELECT u FROM EntityUser u WHERE u.role = 'STUDENT' AND u.status = 'Active' AND " +
            "(:search IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.studentCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.dni) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:idSchool IS NULL OR u.idSchool = :idSchool) " +
            "ORDER BY u.fullName ASC")
    List<EntityUser> findStudentsBySearchAndSchool(
            @Param("search") String search,
            @Param("idSchool") String idSchool);

    @Query("SELECT u FROM EntityUser u WHERE u.role = 'STUDENT' AND u.status = 'Active' AND " +
            "(LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.studentCode) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY u.fullName ASC")
    List<EntityUser> searchStudents(@Param("query") String query);

    @Query("SELECT COUNT(l) FROM EntityLoan l WHERE l.idUser = :idUser AND l.status = 'Active'")
    long countActiveLoans(@Param("idUser") String idUser);

    @Query("SELECT COUNT(l) FROM EntityLoan l WHERE l.idUser = :idUser AND l.status != 'Active'")
    long countInactiveLoans(@Param("idUser") String idUser);
}