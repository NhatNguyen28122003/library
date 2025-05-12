package com.nguyenvannhat.library.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final Properties properties;

    public CustomPermissionEvaluator(Properties properties) {
        this.properties = properties;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject == null || permission == null || authentication == null) {
            return false;
        }
        return hasRole(authentication, targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, targetType, permission);
    }

    private boolean hasRole(Authentication authentication, Object targetDomainObject, Object method) {
        String methodSuffix = method.toString().toUpperCase();

        Set<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role + "_" + methodSuffix)
                .collect(Collectors.toSet());
        String uri = convertUri(targetDomainObject.toString());
        String requiredRole = properties.getProperty(uri);
        return requiredRole != null && authorities.contains(requiredRole);
    }

    private String convertUri(String uri) {
        uri = uri.substring(1);
        return uri.replaceAll("/", ".");
    }
}
