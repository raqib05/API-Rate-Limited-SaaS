package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue
    UUID id;

    String name;

    int requestsPerMinute;
    int requestsPerDay;
    long monthlyQuota;

    @Builder.Default
    private boolean active = true;
}

