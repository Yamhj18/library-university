package com.unamba.apilibrary.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tsystem_config")
@Getter
@Setter
public class EntitySystemConfig {

    @Id
    @Column(name = "id_config")
    private String idConfig;

    @Column(name = "config_key")
    private String configKey;

    @Column(name = "config_value")
    private String configValue;

    @Column(name = "description")
    private String description;

    @Column(name = "updated_at")
    private Date updatedAt;
}
