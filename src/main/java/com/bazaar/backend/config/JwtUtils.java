package com.bazaar.backend.config;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // 🔑 Secure Secret Key (Kam se kam 256-bit lambi string honi chahiye)
    private final String SECRET_STRING = "bazaarEcommerceSecretKeyWindowProductionLevelSecure256BitString";
    private final Key key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());

    // Token validity: 24 Hours (in milliseconds)
    private final long EXPIRATION_TIME = 86400000;

    // 1. Method to Generate JWT Token when user logs in successfully
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. Method to Extract Username from the incoming Token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 3. Method to Validate Token (Check Expiry and Integrity)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("❌ Invalid or Expired JWT Token: " + e.getMessage());
        }
        return false;
    }
}

