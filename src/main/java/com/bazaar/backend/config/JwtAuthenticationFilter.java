package com.bazaar.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Request Header se 'Authorization' header nikalna
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

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
        filterChain.doFilter(request, response);
    }
}
