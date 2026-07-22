package com.unamba.apilibrary.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unamba.apilibrary.entity.EntitySystemConfig;

@Repository
public interface RepositorySystemConfig extends JpaRepository<EntitySystemConfig, String> {
    
    Optional<EntitySystemConfig> findByConfigKey(String configKey);
}
