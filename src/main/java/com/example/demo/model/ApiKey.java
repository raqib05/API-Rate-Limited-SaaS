package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "api_keys")
public class ApiKey {
    static {
        System.out.println(">>> ApiKey ENTITY LOADED");
    }
    @Id
    @GeneratedValue
    UUID id;

    String keyHash;

    @ManyToOne
    Tenant tenant;

    boolean revoked;
    Instant createdAt;
    Instant expiresAt;
}
