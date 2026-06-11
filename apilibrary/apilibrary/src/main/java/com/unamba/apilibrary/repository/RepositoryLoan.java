package com.unamba.apilibrary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unamba.apilibrary.entity.EntityLoan;

@Repository
public interface RepositoryLoan extends JpaRepository<EntityLoan, String> {

    @Query("SELECT l FROM EntityLoan l WHERE " +
            "(:search IS NULL OR LOWER(l.studentName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(l.studentCode) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "l.status = 'Active' " +
            "ORDER BY l.loanDate ASC")
    List<EntityLoan> findActiveBySearch(@Param("search") String search);

    @Query("SELECT l FROM EntityLoan l WHERE " +
            "l.status = 'Active' AND " +
            "l.estimatedReturnDate < CURRENT_DATE " +
            "ORDER BY l.estimatedReturnDate ASC")
    List<EntityLoan> findOverdue();
}