package com.nguyenvannhat.library.configurations;

import com.nguyenvannhat.library.components.CustomMethodSecurityExpressionHandler;
import com.nguyenvannhat.library.filters.JwtFilters;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig extends GlobalMethodSecurityConfiguration implements WebMvcConfigurer {

    private final JwtFilters jwtFilters;
    private final HttpServletRequest httpServletRequest;
    private final RolePropertiesConfig rolePropertiesConfig;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/user/register", "/user/login", "/user/information", "/comments/**", "/post/**").permitAll().anyRequest().authenticated()).addFilterAfter(jwtFilters, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new CustomMethodSecurityExpressionHandler(httpServletRequest, rolePropertiesConfig);
    }
}
