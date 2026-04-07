package com.example.ushi_backend.security;

import com.example.ushi_backend.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    private SecretKey accessTokenKey;
    private SecretKey refreshTokenKey;

    @PostConstruct
    void init() {
        this.accessTokenKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.getAccessTokenSecret()));
        this.refreshTokenKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtProperties.getRefreshTokenSecret()));
    }

    public String generateAccessToken(UserEntity user) {
        return buildToken(user, jwtProperties.getAccessTokenExpirationMs(), accessTokenKey);
    }

    public String generateRefreshToken(UserEntity user) {
        return buildToken(user, jwtProperties.getRefreshTokenExpirationMs(), refreshTokenKey);
    }

    public boolean isAccessTokenValid(String token) {
        return isTokenValid(token, accessTokenKey);
    }

    public boolean isRefreshTokenValid(String token) {
        return isTokenValid(token, refreshTokenKey);
    }

    public String extractEmail(String token) {
        return extractClaims(token, accessTokenKey).getSubject();
    }

    public String extractEmailFromRefreshToken(String token) {
        return extractClaims(token, refreshTokenKey).getSubject();
    }

    private String buildToken(UserEntity user, long expirationMs, SecretKey key) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenValid(String token, SecretKey key) {
        try {
            extractClaims(token, key);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private Claims extractClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
