package com.example.demo.controller;

import com.example.demo.model.Tenant;
import com.example.demo.service.ApiKeyService;
import com.example.demo.service.TenantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class ApiKeyController {
    ApiKeyService apiKeyService;
    TenantService tenantService;

    @PostMapping("/test/createKey")
    public String createKeyTest(@RequestParam UUID tenantID){
//        Tenant tenant = tenantService.findbyId(tenantID);
        return apiKeyService.createKey(tenantID);
    }

    @GetMapping("/ping")
    public String ping(){
        String ret = "ping";
        return ret;
    }
}
