package com.unamba.apilibrary.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tprofessional_school") // o "professional_schools" si quieres usar tu diseño original
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityFaculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFaculty;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(length = 120)
    private String description;

    @Column(length = 50)
    private String status; // ACTIVE, INACTIVE

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}