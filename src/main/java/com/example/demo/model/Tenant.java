package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
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
    @JsonIgnore
    List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "tenant")
    @JsonIgnore
    List<ApiKey> apiKeys = new ArrayList<>();

    @ManyToOne(optional = false)
    Plan plan;

    Instant createdAt;

    public void addApiKey(ApiKey apiKey) {
        apiKeys.add(apiKey);
        apiKey.setTenant(this);
    }
}
