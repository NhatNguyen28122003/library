package com.nguyenvannhat.library.exceptions;

import com.nguyenvannhat.library.responses.CustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalException {
    private final MessageSource messageSource;
    private final Logger logger = Logger.getLogger(GlobalException.class.getName());

    @ExceptionHandler(IOException.class)
    public <T> ResponseEntity<CustomResponse<T>> handleIOException(IOException e) {
        logger.info(e.getMessage());
        return ResponseEntity.badRequest().body(CustomResponse.error(1000, "???"));
    }

    @ExceptionHandler(ApplicationException.class)
    public <T> ResponseEntity<CustomResponse<T>> handleApplicationException(ApplicationException e) {
        logger.info(e.getMessage());
        return ResponseEntity.badRequest().body(CustomResponse.error(HttpStatus.BAD_REQUEST.value(), messageSource.getMessage(e.getMessage(), null, Locale.ENGLISH)));
    }
}
