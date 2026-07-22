package com.unamba.apilibrary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unamba.apilibrary.entity.EntityLoan;

@Repository
public interface RepositoryLoan extends JpaRepository<EntityLoan, String> {

    @Query("SELECT l FROM EntityLoan l JOIN l.parentUser u WHERE " +
            "(:search IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.studentCode) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "l.status = 'Active' " +
            "ORDER BY l.loanDate ASC")
    List<EntityLoan> findActiveBySearch(@Param("search") String search);

    @Query("SELECT l FROM EntityLoan l WHERE " +
            "l.status = 'Active' AND " +
            "l.estimatedReturnDate < CURRENT_DATE " +
            "ORDER BY l.estimatedReturnDate ASC")
    List<EntityLoan> findOverdue();
    
    @Query("SELECT l FROM EntityLoan l WHERE l.idUser = :idUser ORDER BY l.loanDate DESC")
    List<EntityLoan> findByIdUser(@Param("idUser") String idUser);
    
    @Query("SELECT l FROM EntityLoan l WHERE l.idUser = :idUser AND l.status = 'Active'")
    List<EntityLoan> findActiveByIdUser(@Param("idUser") String idUser);

    @Query("SELECT l FROM EntityLoan l ORDER BY l.createdAt DESC")
    List<EntityLoan> findAllHistory();

    @Query("SELECT l FROM EntityLoan l JOIN l.parentUser u WHERE " +
            "(:search IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.studentCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(l.code) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:idSchool IS NULL OR u.idSchool = :idSchool) AND " +
            "(:status IS NULL OR l.status = :status) AND " +
            "(l.status = 'Active' OR l.status = 'Overdue') " +
            "ORDER BY l.loanDate ASC")
    List<EntityLoan> findActiveBySearchAndFilters(
            @Param("search") String search,
            @Param("idSchool") String idSchool,
            @Param("status") String status);
}