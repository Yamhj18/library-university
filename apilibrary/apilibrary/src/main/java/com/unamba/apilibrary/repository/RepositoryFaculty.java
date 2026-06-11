package com.unamba.apilibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unamba.apilibrary.entity.EntityFaculty;

@Repository
public interface RepositoryFaculty extends JpaRepository<EntityFaculty, String> {
}