package com.bazaar.backend.controller;

import com.bazaar.backend.config.JwtUtils;
import com.bazaar.backend.dto.AuthResponse;
import com.bazaar.backend.dto.LoginRequest;
import com.bazaar.backend.dto.RegisterRequest;
import com.bazaar.backend.model.User;
import com.bazaar.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Isse saare final fields ka constructor Lombok khud bana dega
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // ==========================================
    // 1. SPRINT 7: USER REGISTRATION (NEW)
    // ==========================================
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());

        // Password ko hash karke DB me bhej rahe hain
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }

    // ==========================================
    // 2. SPRINT 7: REAL USER LOGIN (NEW)
    // ==========================================
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        String jwt = jwtUtils.generateToken(authentication.getName());
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    // ==========================================
    // 3. SPRINT 6: TEMPORARY TEST ENDPOINT (PREVIOUS)
    // ==========================================
    @GetMapping("/test-token")
    public ResponseEntity<Map<String, String>> generateTestToken(@RequestParam String username) {
        String token = jwtUtils.generateToken(username);

        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("jwt_token", token);
        response.put("token_type", "Bearer");

        return ResponseEntity.ok(response);
    }
}
