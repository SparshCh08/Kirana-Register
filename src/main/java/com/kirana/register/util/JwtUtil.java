package com.kirana.register.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.function.Function;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${auth.secret-key}")
    private String SECRET_KEY;

    // Method to extract the username from JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract claims from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(); // Convert your secret key to bytes
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Validate the JWT token by checking the username
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract the expiration date from JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generate token with username and role
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // Add user role to claims
        return createToken(claims, username);
    }

    // Method to create token
    private String createToken(Map<String, Object> claims, String subject) {
        long expirationTimeMillis = 1000 * 60 * 60 * 10; // 10 hours
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTimeMillis);

        claims.put(Claims.SUBJECT, subject);
        claims.put(Claims.ISSUED_AT, issuedAt);
        claims.put(Claims.EXPIRATION, expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(getSigningKey()) // Use getSigningKey method
                .compact();
    }

    // Extract role from JWT
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class)); // Use claims resolver
    }

    // Check if user has the specified role
    public boolean hasRole(String token, String role) {
        final String extractedRole = extractRole(token);
        return extractedRole.equals(role);
    }
}
