package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    UUID id;

    @Column(unique = true)
    String email;

    String passwordHash;

    @ManyToOne(optional = false)
    Tenant tenant;

    Instant createdAt;
}

