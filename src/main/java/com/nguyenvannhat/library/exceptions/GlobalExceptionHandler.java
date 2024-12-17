package com.nguyenvannhat.library.exceptions;

import com.nguyenvannhat.library.responses.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public CustomResponse<?> handleDataNotFoundException(DataNotFoundException e) {
        return new CustomResponse<>(HttpStatus.NOT_FOUND, e.getMessage(), null);
    }

    @ExceptionHandler(InvalidDataException.class)
    public CustomResponse<?> handleInvalidDataException(InvalidDataException e) {
        return new CustomResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public CustomResponse<?> handleInvalidTokenException(InvalidTokenException e) {
        return new CustomResponse<>(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
    }

    @ExceptionHandler(PermissionException.class)
    public CustomResponse<?> handlePermissionException(PermissionException e) {
        return new CustomResponse<>(HttpStatus.FORBIDDEN, e.getMessage(), null);
    }

    @ExceptionHandler(TokenGenerationException.class)
    public CustomResponse<?> handleTokenGenerationException(TokenGenerationException e) {
        return new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
    }
}
