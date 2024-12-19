package com.nguyenvannhat.library.components;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages_en","messages_vi");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
