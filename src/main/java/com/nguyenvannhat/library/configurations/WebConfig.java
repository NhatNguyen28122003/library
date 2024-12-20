package com.nguyenvannhat.library.configurations;

import com.nguyenvannhat.library.components.CustomMethodSecurityExpressionHandler;
import com.nguyenvannhat.library.filters.JwtFilters;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity// Use @EnableMethodSecurity instead of the deprecated @EnableGlobalMethodSecurity
@RequiredArgsConstructor
public class WebConfig {

    private final JwtFilters jwtFilters;
    private final HttpServletRequest httpServletRequest;
    private final RolePropertiesConfig rolePropertiesConfig;

    /**
     * Cấu hình chuỗi lọc bảo mật.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Vô hiệu hóa CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/register", "/user/login", "/user/information", "/comments/**", "/post/**")
                        .permitAll() // Các endpoint công khai
                        .anyRequest()
                        .authenticated() // Các endpoint khác yêu cầu xác thực
                )
                .addFilterAfter(jwtFilters, UsernamePasswordAuthenticationFilter.class) // Thêm bộ lọc JWT
                .build();
    }

    /**
     * Cấu hình xử lý biểu thức bảo mật tùy chỉnh.
     */
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        return new CustomMethodSecurityExpressionHandler(httpServletRequest, rolePropertiesConfig);
    }
}
