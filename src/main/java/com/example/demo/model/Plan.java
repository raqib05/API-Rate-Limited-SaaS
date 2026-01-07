package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Table(name = "plans")
public class Plan {

    @Id
    UUID id;

    String name;

    int requestsPerMinute;
    int requestsPerDay;
    long monthlyQuota;

    boolean active;
}

