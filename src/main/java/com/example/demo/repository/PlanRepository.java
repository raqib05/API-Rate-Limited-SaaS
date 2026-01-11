package com.example.demo.repository;

import com.example.demo.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    Plan findPlanById(UUID id);
}
