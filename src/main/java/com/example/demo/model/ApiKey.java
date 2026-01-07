package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import jakarta.persistence.Table;
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
    UUID id;
    String keyHash;

    @ManyToOne
    Tenant tenant;

    boolean revoked;
    Instant createdAt;
    Instant expiresAt;
}
