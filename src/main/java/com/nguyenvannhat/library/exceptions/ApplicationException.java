package com.nguyenvannhat.library.exceptions;


import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getLocalizedMessage(MessageSource messageSource) {
        return messageSource.getMessage(errorCode.getCode(), null, LocaleContextHolder.getLocale());
    }
}
