package com.nguyenvannhat.library.configurations;

import com.nguyenvannhat.library.filters.JwtFilters;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebConfig {
    private final JwtFilters jwtFilters;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Vô hiệu hóa CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/register", "/user/login", "/user/information", "/comments/**", "/post/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .addFilterAfter(jwtFilters, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/messages_en", "classpath:i18n/messages_vi");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public Properties properties() throws IOException {
        InputStream inputStream = new ClassPathResource("roles.properties").getInputStream();
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() throws IOException {
        return new CustomMethodSecurityExpressionHandler(properties());
    }
}
