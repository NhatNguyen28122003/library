package com.nguyenvannhat.library.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class WebConfig {

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
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() throws IOException {
        return new CustomMethodSecurityExpressionHandler(properties());
    }
}
