package com.nguyenvannhat.library.components;

import com.nguyenvannhat.library.configurations.CustomMethodSecurityExpressionRoot;
import com.nguyenvannhat.library.configurations.RolePropertiesConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final HttpServletRequest httpServletRequest;
    private final RolePropertiesConfig rolePropertiesConfig;

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        return new CustomMethodSecurityExpressionRoot(authentication, httpServletRequest, rolePropertiesConfig);
    }
}
