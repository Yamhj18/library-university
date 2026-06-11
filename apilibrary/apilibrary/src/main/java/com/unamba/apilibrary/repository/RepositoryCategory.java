package com.unamba.apilibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unamba.apilibrary.entity.EntityCategory;

@Repository
public interface RepositoryCategory extends JpaRepository<EntityCategory, String> {
}