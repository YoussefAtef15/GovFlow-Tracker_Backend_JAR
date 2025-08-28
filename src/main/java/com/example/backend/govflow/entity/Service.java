package com.example.backend.govflow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;

@Entity
@Table(name = "services")
@Getter
@Setter
public class Service implements org.springframework.stereotype.Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Override
    public String value() {
        return "";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}