package com.nguyenvannhat.library.configurations;

import com.nguyenvannhat.library.components.CustomMethodSecurityExpressionRoot;
import com.nguyenvannhat.library.components.CustomPermissionEvaluator;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.util.Properties;

public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final Properties properties;

    public CustomMethodSecurityExpressionHandler(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        CustomMethodSecurityExpressionRoot root =
                new CustomMethodSecurityExpressionRoot(authentication);
        root.setPermissionEvaluator(new CustomPermissionEvaluator(properties));
        return root;
    }
}
