package com.nguyenvannhat.library.exceptions;

import com.nguyenvannhat.library.responses.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> handleDataNotFoundException(ResponseStatus status, DataNotFoundException e) {
        return CustomResponse.error(status, e.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<?> handleInvalidDataException(ResponseStatus status, InvalidDataException e) {
        return CustomResponse.error(status, e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidTokenException(ResponseStatus status, InvalidTokenException e) {
        return CustomResponse.error(status, e.getMessage());
    }

    @ExceptionHandler(PermissionException.class)
    public ResponseEntity<?> handlePermissionException(ResponseStatus status, PermissionException e) {
        return CustomResponse.error(status, e.getMessage());
    }

    @ExceptionHandler(TokenGenerationException.class)
    public ResponseEntity<?> handleTokenGenerationException(ResponseStatus status, TokenGenerationException e) {
        return CustomResponse.error(status, e.getMessage());
    }
}
