package com.nguyenvannhat.library.components;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Date;
import java.time.Instant;
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

    public String generateToken(User user) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userName", user.getUserName());
            return Jwts.builder()
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .setClaims(claims)
                    .setSubject(user.getUserName())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .compact();
        } catch (ApplicationException e) {
            throw new ApplicationException(Constant.ERROR_GENERATE_TOKEN);
        }
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

    public boolean validateToken(String token) {
        return getClaim(token, Claims::getExpiration).toInstant().isAfter(Instant.now());
    }


    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
