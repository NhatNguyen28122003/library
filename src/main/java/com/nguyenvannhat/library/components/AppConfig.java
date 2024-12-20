package com.nguyenvannhat.library.components;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    public MessageSource messageSource() {
         ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
         messageSource.setBasenames("i18n/messages_en", "i18n/messages_vi");
         messageSource.setDefaultEncoding("UTF-8");
         return messageSource;
    }
}
