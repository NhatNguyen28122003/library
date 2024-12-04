package com.nguyenvannhat.library.component;

import com.nguyenvannhat.library.models.User;
import com.nguyenvannhat.library.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final UserRepository userRepository;
    @Value("${jwt.key}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(User user) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("phoneNumber", user.getPhoneNumber());
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .setExpiration(new Date(System.currentTimeMillis() +  expiration * 1000L))
                    .setIssuedAt(new Date())
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Don't create token");
        }
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean validateToken(String token) {
        return extractAllClaims(token).getExpiration().before(new Date(System.currentTimeMillis()));
    }
}
