package com.example.demo.controller;


import com.example.demo.dto.CreateTenantDTO;
import com.example.demo.model.Tenant;
import com.example.demo.service.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class TenantController {
    TenantService tenantService;

    @PostMapping("/test/createTenant")
    public Tenant createTenant(@RequestBody CreateTenantDTO request) {
        return tenantService.addTenant(request.getName(), request.getPlanId());
    }

    @GetMapping("/test/getTenant")
    public Tenant getTenant(@RequestParam UUID tenantID){
        return tenantService.findbyId(tenantID);
    }

}
