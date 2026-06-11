package com.bazaar.backend.controller;

import com.bazaar.backend.config.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtils jwtUtils;

    // Constructor Injection for Security Beans
    public AuthController(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @GetMapping("/test-token")
    public ResponseEntity<Map<String, String>> generateTestToken(@RequestParam String username) {
        // 1. Generate token dynamically for the requested user
        String token = jwtUtils.generateToken(username);

        // 2. Wrap it inside a clean response structure
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("jwt_token", token);
        response.put("token_type", "Bearer");

        return ResponseEntity.ok(response);
    }
}
