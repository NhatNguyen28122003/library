package com.nguyenvannhat.library.filters;


import com.nguyenvannhat.library.component.JwtUtils;
import com.nguyenvannhat.library.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    @Value("${apiPrefix}")
    private String apiPrefix;
    @Value("${jwt.key}")
    private String secretKey;

    private final JwtUtils jwtUtils;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("Request URI: {}", request.getRequestURI());
            log.info("Authorization Header: {}", request.getHeader("Authorization"));

            if (isBypassToken(request)) {
                log.info("Bypass token for request: {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            final String token = authHeader.substring(7);
            final String phoneNumber = jwtUtils.extractPhoneNumber(token);
            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("Valid token for user: {}", phoneNumber);
                User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error during filtering: ", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/roles/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/**", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();
        for (Pair<String, String> bypassToken : bypassTokens) {
            if (requestPath.contains(bypassToken.getFirst())
                    && requestMethod.equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
}
