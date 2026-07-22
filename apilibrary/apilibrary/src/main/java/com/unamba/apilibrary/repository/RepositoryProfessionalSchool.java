package com.unamba.apilibrary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unamba.apilibrary.entity.EntityProfessionalSchool;

@Repository
public interface RepositoryProfessionalSchool extends JpaRepository<EntityProfessionalSchool, String> {

    @Query("SELECT s FROM EntityProfessionalSchool s WHERE s.status = 'Active' ORDER BY s.name ASC")
    List<EntityProfessionalSchool> findAllActive();

    boolean existsByName(String name);

    List<EntityProfessionalSchool> findByStatus(String status);
}
