package com.example.demo.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1) // Run filter early
public class RateLimitingFilter implements Filter {

    // Store buckets by IP address
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Policy: 5 requests per 1 minute
    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.builder().capacity(5).refillGreedy(5, Duration.ofMinutes(1)).build();
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Only rate limit the POST /login endpoint
        if (httpRequest.getRequestURI().equals("/login") && httpRequest.getMethod().equalsIgnoreCase("POST")) {
            String clientIp = httpRequest.getRemoteAddr();
            
            // Get or create bucket for this IP
            Bucket bucket = buckets.computeIfAbsent(clientIp, k -> createNewBucket());
            
            // Try consume 1 token
            ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
            
            if (!probe.isConsumed()) {
                // Rate limit exceeded!
                httpResponse.setStatus(429); // Too Many Requests
                httpResponse.getWriter().write("Too many login attempts. Please try again later.");
                return; // Block the request
            }
        }
        
        // Continue filter chain if allowed
        chain.doFilter(request, response);
    }
}
