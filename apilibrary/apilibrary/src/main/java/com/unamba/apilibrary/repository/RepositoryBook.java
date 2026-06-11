package com.unamba.apilibrary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unamba.apilibrary.entity.EntityBook;

@Repository
public interface RepositoryBook extends JpaRepository<EntityBook, String> {

    @Query("SELECT b FROM EntityBook b WHERE " +
            "(:search IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(b.code) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:idCategory IS NULL OR b.idCategory = :idCategory)")
    List<EntityBook> findBySearchAndCategory(
            @Param("search") String search,
            @Param("idCategory") String idCategory);
}