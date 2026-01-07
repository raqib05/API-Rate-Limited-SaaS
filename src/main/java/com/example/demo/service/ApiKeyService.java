package com.example.demo.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import com.example.demo.model.ApiKey;
import com.example.demo.model.Tenant;
import org.springframework.stereotype.Service;
import com.example.demo.repository.ApiKeyRepository;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;

    private String generateSecureKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Transactional
    public String createKey(Tenant tenant) {
        String rawKey = generateSecureKey();
        String keyHash = DigestUtils.sha256Hex(rawKey);

        ApiKey apiKey = new ApiKey();
        apiKey.setKeyHash(keyHash);
        apiKey.setTenant(tenant);
        apiKey.setRevoked(false);
        tenant.addApiKey(apiKey);
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
