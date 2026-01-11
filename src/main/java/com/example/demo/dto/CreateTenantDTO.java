package com.example.demo.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateTenantDTO {
    private String name;
    private UUID planId;
}
