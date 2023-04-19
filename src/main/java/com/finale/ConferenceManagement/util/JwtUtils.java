package com.finale.ConferenceManagement.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

//    @Value("${jwt.secretKey}")
//    private String keyString;

    private final SecretKey key;

    @Value("${jwt.expiration.time}")
    private int jwtExpirationInMs;

    public int getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }

    public int getJwtExpirationInSec() {
        return jwtExpirationInMs / 1000;
    }

    public JwtUtils() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        this.jwtExpirationInMs = 1000 * 60 * 60 * 24;
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Jws<Claims> jws;
        try {
            jws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            // We can safely trust the JWT
            return jws.getBody().getSubject();
        } catch (JwtException e) {
            // We cannot use the JWT
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }

    public String getJwtFromAuthHeader(String authContent) {
        if (authContent != null && authContent.startsWith("Bearer ")) {
            return authContent.substring(7);
        }

        return null;
    }
}
