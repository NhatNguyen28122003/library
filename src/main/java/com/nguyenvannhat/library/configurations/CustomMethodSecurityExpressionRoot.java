package com.nguyenvannhat.library.configurations;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * Custom security expression root to validate access based on role mappings and request URIs.
 */
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private static final Logger logger = LoggerFactory.getLogger(CustomMethodSecurityExpressionRoot.class);
    private final HttpServletRequest httpServletRequest;
    private final RolePropertiesConfig rolePropertiesConfig;

    /**
     * Constructor to initialize security root with required dependencies.
     *
     * @param authentication       The current user authentication
     * @param httpServletRequest   The current HTTP servlet request
     * @param rolePropertiesConfig Configuration for role mappings
     */
    public CustomMethodSecurityExpressionRoot(Authentication authentication, HttpServletRequest httpServletRequest, RolePropertiesConfig rolePropertiesConfig) {
        super(authentication);
        this.httpServletRequest = httpServletRequest;
        this.rolePropertiesConfig = rolePropertiesConfig;
    }

    /**
     * Validates if the current user has the required role for the requested URI.
     *
     * @return true if the user has the required role, false otherwise.
     * @throws AccessDeniedException when no role mapping is found.
     */
    public boolean fileRole() {
        String uri = convertUri(httpServletRequest.getRequestURI());
        String requiredRole = rolePropertiesConfig.getRoleForUri(uri);

        if (requiredRole == null) {
            logger.error("No role mapping found for URI: {}", uri);
            throw new AccessDeniedException("No role mapping found for URI: " + uri);
        }

        logger.info("Validating role for URI: {}. Required role: {}", uri, requiredRole);
        boolean hasRequiredRole = hasRole(requiredRole);

        if (!hasRequiredRole) {
            logger.warn("Access denied. User does not have role: {}", requiredRole);
        }

        return hasRequiredRole;
    }

    /**
     * Converts the given URI to a dot-separated string for role mapping.
     *
     * @param uri The original request URI.
     * @return Converted URI string.
     */
    private String convertUri(String uri) {
        if (uri == null || uri.isEmpty()) {
            return "";
        }
        // Remove leading '/' and replace '/' with '.'
        return uri.substring(1).replace('/', '.');
    }

    @Override
    public Object getFilterObject() {
        return null;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        // Not implemented
    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        // Not implemented
    }

    @Override
    public Object getThis() {
        return this;
    }
}
