package com.nguyenvannhat.library.exceptions;

import com.nguyenvannhat.library.components.AppConfig;
import com.nguyenvannhat.library.responses.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalException {
    private final AppConfig appConfig;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        String errorMessage = e.getLocalizedMessage(appConfig.messageSource());
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getCode(), errorMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException() {
        String message = appConfig.messageSource().getMessage(ErrorCode.INTERNAL_SERVER.getCode(), null, Locale.getDefault());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER.getCode(), message);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
