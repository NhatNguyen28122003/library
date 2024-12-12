package com.nguyenvannhat.library.components;

import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.exceptions.TokenGenerationException;
import com.nguyenvannhat.library.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final UserRepository userRepository;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(User user) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userName", user.getUsername());
            return Jwts.builder()
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .setClaims(claims)
                    .setSubject(user.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .compact();
        } catch (Exception e) {
            throw new TokenGenerationException("Could not generate token!");
        }
    }

    public OffsetDateTime getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration().toInstant().atOffset(ZoneOffset.UTC);
    }


    public Claims getClaimsFromToken(String token) {
        return (Claims) Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parse(token)
                .getBody();
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUserNameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        //Ngày hết hạn phải sau ngày hôm nay thì mới sử dụng được token
        return getClaim(token, Claims::getExpiration).toInstant().isAfter(Instant.now())
                && userDetails.getUsername().equals(getUserNameFromToken(token));
    }
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
