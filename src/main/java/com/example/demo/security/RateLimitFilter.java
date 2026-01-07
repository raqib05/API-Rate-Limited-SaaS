package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import com.example.demo.model.Plan;
import com.example.demo.model.Tenant;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final StringRedisTemplate redisTemplate;

    private static final DefaultRedisScript<Long> TOKEN_BUCKET_SCRIPT;

    static {
        TOKEN_BUCKET_SCRIPT = new DefaultRedisScript<>();
        TOKEN_BUCKET_SCRIPT.setResultType(Long.class);
        TOKEN_BUCKET_SCRIPT.setScriptText("""
            local bucket = redis.call("HMGET", KEYS[1], "tokens", "last_refill")
            local tokens = tonumber(bucket[1])
            local last_refill = tonumber(bucket[2])

            if tokens == nil then
                tokens = tonumber(ARGV[1])
                last_refill = tonumber(ARGV[3])
            end

            local now = tonumber(ARGV[3])
            local elapsed = math.max(0, now - last_refill)
            local refill = elapsed * tonumber(ARGV[2])
            tokens = math.min(tonumber(ARGV[1]), tokens + refill)

            if tokens < 1 then
                redis.call("HMSET", KEYS[1], "tokens", tokens, "last_refill", now)
                return 0
            end

            tokens = tokens - 1
            redis.call("HMSET", KEYS[1], "tokens", tokens, "last_refill", now)
            return 1
        """);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/test")
                || path.startsWith("/actuator");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        Tenant tenant = (Tenant) request.getAttribute("tenant");
        Plan plan = (Plan) request.getAttribute("plan");
        UUID apiKeyId = (UUID) request.getAttribute("apiKeyId");

        if (tenant == null || plan == null || apiKeyId == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Rate limit context missing");
            return;
        }

        int capacity = plan.getRequestsPerMinute();
        double refillRate = capacity / 60.0; // tokens per second
        long now = Instant.now().getEpochSecond();

        String key = "bucket:apikey:" + apiKeyId;

        Long allowed = redisTemplate.execute(
                TOKEN_BUCKET_SCRIPT,
                Collections.singletonList(key),
                String.valueOf(capacity),
                String.valueOf(refillRate),
                String.valueOf(now)
        );

        if (allowed == null || allowed == 0) {
            response.setStatus(429);
            response.getWriter().write("Rate limit exceeded");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
