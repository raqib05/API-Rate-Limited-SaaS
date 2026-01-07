package com.example.demo.security;

import com.example.demo.service.ApiKeyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import com.example.demo.model.ApiKey;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.demo.repository.ApiKeyRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    ApiKeyService apiKeyService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/test")
                || path.startsWith("/actuator");
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    jakarta.servlet.FilterChain filterChain)
            throws ServletException, IOException {

        String key = request.getHeader("X-API-KEY");

        if (key == null || key.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing API key");
            return;
        }

        String hash = DigestUtils.sha256Hex(key);
        Optional<ApiKey> result = apiKeyService.findByKeyHash(hash);

        if (result.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API key");
            return;
        }

        ApiKey apiKey = result.get();

        if (apiKey.isRevoked()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Revoked API key");
            return;
        }

        Instant expiration = apiKey.getExpiresAt();
        if (expiration != null && expiration.isBefore(Instant.now())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Expired API key");
            return;
        }

        request.setAttribute("tenant", apiKey.getTenant());
        request.setAttribute("plan", apiKey.getTenant().getPlan());
        request.setAttribute("apiKeyId", apiKey.getId());

        filterChain.doFilter(request, response);
    }
}



