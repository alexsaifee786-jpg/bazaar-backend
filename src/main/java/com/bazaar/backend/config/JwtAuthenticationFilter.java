package com.bazaar.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
<<<<<<< HEAD
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
=======
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;
>>>>>>> feature/BAZ-sprint7-auth-lifecycle

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

<<<<<<< HEAD
        // 1. HTTP Request Header se 'Authorization' key nikalna
=======
        // 1. Request Header se 'Authorization' header nikalna
>>>>>>> feature/BAZ-sprint7-auth-lifecycle
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

<<<<<<< HEAD
        // 2. Check karna ki header 'Bearer ' se shuru ho raha hai ya nahi
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // 'Bearer ' ke baad ka asli token string cut kiya
            if (jwtUtils.validateToken(token)) {
                username = jwtUtils.getUsernameFromToken(token); // Token se username nikala
            }
        }

        // 3. Agar username mil gaya aur SecurityContext me pehle se koi logged-in user nahi hai
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Core Spring Security token object taiyar karna (Bina password ke, privileges khali rakhenge abhi)
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 🌟 Spring Security Context Memory Register me user ko logged-in mark kar dena!
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 4. Request ko aage agle filters ya controller ke paas bhej dena
=======
        // 2. Check karna ki header 'Bearer ' se start ho raha hai ya nahi
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // 'Bearer ' ke baad ka real token extract karna
            try {
                // JwtUtils ka use karke token se username nikalna (Pichle sprint ka logic)
                username = jwtUtils.getUsernameFromToken(token);
            } catch (Exception e) {
                logger.error("Token se username extract karne me dikkat aayi: " + e.getMessage());
            }
        }

        // 3. Agar username mil gaya aur SecurityContext me abhi tak koi auth nahi hai
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // JwtUtils ka token validation check (Ensure karein aapke pass validateToken ya equivalent method ho)
            if (jwtUtils.validateToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Spring Security ko batana ki yeh user valid hai!
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 4. Request ko aage badhana agle filter ki taraf
>>>>>>> feature/BAZ-sprint7-auth-lifecycle
        filterChain.doFilter(request, response);
    }
}
