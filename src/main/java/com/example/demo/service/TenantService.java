package com.example.demo.service;

import com.example.demo.repository.PlanRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import com.example.demo.model.Plan;
import com.example.demo.model.Tenant;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;
import com.example.demo.repository.TenantRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TenantService {
    private final TenantRepository tenantRepository;
    private final PlanRepository planRepository;

    @Transactional
    public Tenant addTenant(String name, UUID planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Tenant tenant = new Tenant();
        tenant.setId(UUID.randomUUID());
        tenant.setName(name);
        tenant.setPlan(plan);
        tenant.setCreatedAt(Instant.now());

        return tenantRepository.save(tenant);
    }

//    @Transactional
//    public Tenant addApiKey(ApiKey apiKey, UUID tenantID){
//        Optional<Tenant> res = tenantRepository.findById(tenantID);
//        if (res.isPresent()) {
//            Tenant tenant = res.get();
//            List<ApiKey> apiKeys = tenant.getApiKeys();
//            apiKeys.add(apiKey);
//            tenantRepository.save(tenant);
//            return tenant;
//        }
//        return null;
//    }

    @Transactional
    public Tenant addUser(User user, UUID tenantID){
        Optional<Tenant> res = tenantRepository.findById(tenantID);
        if (res.isPresent()) {
            Tenant tenant = res.get();
            List<User> users = tenant.getUsers();
            users.add(user);
            tenantRepository.save(tenant);
            return tenant;
        }
        return null;
    }

    @Transactional
    public Tenant updatePlan(Plan plan, UUID tenantID){
        Optional<Tenant> res = tenantRepository.findById(tenantID);
        if (res.isPresent()) {
            Tenant tenant = res.get();
            tenant.setPlan(plan);
            tenantRepository.save(tenant);
            return tenant;
        }
        return null;
    }

    public Tenant findbyId(UUID id){
        return tenantRepository.findById(id).orElse(null);
    }


}
