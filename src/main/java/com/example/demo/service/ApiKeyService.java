package com.example.demo.service;

import com.example.demo.repository.TenantRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import com.example.demo.model.ApiKey;
import com.example.demo.model.Tenant;
import org.springframework.stereotype.Service;
import com.example.demo.repository.ApiKeyRepository;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    private final TenantRepository tenantRepository;

    private String generateSecureKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Transactional
    public String createKey(UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        String rawKey = UUID.randomUUID().toString().replace("-", "");
        String keyHash = DigestUtils.sha256Hex(rawKey);

        ApiKey apiKey = new ApiKey();
        apiKey.setKeyHash(keyHash);
        apiKey.setTenant(tenant);   // owning side
        apiKey.setRevoked(false);
        apiKey.setCreatedAt(Instant.now());

        apiKeyRepository.save(apiKey);

        return rawKey;
    }
    @Transactional
    public void revokeKey(String key) {
        String hash = DigestUtils.sha256Hex(key);

        Optional<ApiKey> res = apiKeyRepository.findByKeyHash(hash);

        if (res.isPresent()) {
            ApiKey apiKey = res.get();
            apiKey.setRevoked(true);
            apiKeyRepository.save(apiKey);
        }
    }
    public Optional<ApiKey> findByKeyHash(String keyHash){
        return apiKeyRepository.findByKeyHash(keyHash);
    }
}
