package com.nguyenvannhat.library.components;

import com.nguyenvannhat.library.entities.UserCustom;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.exceptions.ErrorCode;
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

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(UserCustom userCustom) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userName", userCustom.getUsername());
            return Jwts.builder()
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .setClaims(claims)
                    .setSubject(userCustom.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .compact();
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.TOKEN_NOT_CREATE);
        }
    }

    public OffsetDateTime getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration().toInstant().atOffset(ZoneOffset.UTC);
    }


    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
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
        return getClaim(token, Claims::getExpiration).toInstant().isAfter(Instant.now())
                && userDetails.getUsername().equals(getUserNameFromToken(token));
    }


    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
