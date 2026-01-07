package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name="tenants")
public class Tenant {

    @Id
    UUID id;

    String name;

    @OneToMany(mappedBy = "tenant")
    List<User> users;

    @OneToMany(mappedBy = "tenant")
    List<ApiKey> apiKeys;

    @ManyToOne(optional = false)
    Plan plan;

    Instant createdAt;

    public void addApiKey(ApiKey apiKey) {
        apiKeys.add(apiKey);
        apiKey.setTenant(this);
    }
}
