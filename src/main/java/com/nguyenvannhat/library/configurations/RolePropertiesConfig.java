package com.nguyenvannhat.library.configurations;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class RolePropertiesConfig {

    private final Properties roleMappings = new Properties();

    @PostConstruct
    public void loadRoleMappings() throws IOException {
        InputStream input = new ClassPathResource("roles.properties").getInputStream();
        roleMappings.load(input);
    }

    public String getRoleForUri(String uri) {
        for (String key : roleMappings.stringPropertyNames()) {
            if (uri.contains(key)) {
                return roleMappings.getProperty(key);
            }
        }
        return null;
    }
}
