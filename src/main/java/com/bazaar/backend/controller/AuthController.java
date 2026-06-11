package com.bazaar.backend.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/test")
    public String testPublicEndpoint() {
        return "🚀 Access Granted: This endpoint is completely Public and Whitelisted!";
    }
}

