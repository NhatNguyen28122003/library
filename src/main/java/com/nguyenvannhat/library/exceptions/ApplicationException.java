package com.nguyenvannhat.library.exceptions;


import lombok.Getter;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Getter
public class ApplicationException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getLocalizedMessage(MessageSource messageSource, Object... args) {
        return messageSource.getMessage(errorCode.getCode(), args, Locale.ENGLISH);
    }
}
