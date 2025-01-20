package com.nguyenvannhat.library.components;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Properties;


public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final Properties properties;

    public CustomPermissionEvaluator(Properties properties) {
        this.properties = properties;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object uri, Object method) {
        if (authentication == null || uri == null || method == null) {
            return false;
        }
        return hasRole(authentication, uri);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String uri, Object method) {
        if (authentication == null || uri == null || method == null) {
            return false;
        }

        return hasRole(authentication, uri);
    }

    private boolean hasRole(Authentication authentication, Object targetDomainObject) {
        authentication.getAuthorities();
        String uri = convertString((String) targetDomainObject);
        for (Object key : properties.keySet()) {
            if (uri.contains(key.toString().substring("role".length()))
                    && authentication.getAuthorities().contains(
                    new SimpleGrantedAuthority(
                            (properties.getProperty((String) key))
                    )
            )) {
                return true;
            }
        }

        return false;
    }

    private String convertString(String target) {
        return target.replace("/", ".");
    }
}
