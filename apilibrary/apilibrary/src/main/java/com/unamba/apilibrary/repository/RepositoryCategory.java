package com.unamba.apilibrary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.unamba.apilibrary.entity.EntityCategory;

@Repository
public interface RepositoryCategory extends JpaRepository<EntityCategory, String> {
    
    @Query("SELECT c FROM EntityCategory c WHERE c.status = 'Active' ORDER BY c.name ASC")
    List<EntityCategory> findAllActive();
}