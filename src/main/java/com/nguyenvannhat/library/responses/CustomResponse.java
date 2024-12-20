package com.nguyenvannhat.library.responses;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

@Getter
@Setter
public class CustomResponse<T> {
    private String statusCode;
    private String statusMessage;
    private T data;

    public CustomResponse(String statusCode, String statusMessage, T data) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.data = data;
    }

    public static <T> ResponseEntity<CustomResponse<T>> success(HttpStatus status,SuccessCode successCode, MessageSource messageSource, T data) {
        String message = messageSource.getMessage(successCode.getCode(),null,Locale.getDefault());
        CustomResponse<T> customResponse = new CustomResponse<T>(successCode.getCode(), message, data);
        return ResponseEntity.status(status).body(customResponse);
    }

    public static <T> ResponseEntity<CustomResponse<T>> download(T data) {
        CustomResponse<T> customResponse = new CustomResponse<T>("jjjjjj", "OK", data);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }
}
