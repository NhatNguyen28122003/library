package com.nguyenvannhat.library.responses;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class CustomResponse<T> {
    private int statusCode;
    private String statusMessage;
    private T data;

    public CustomResponse(int statusCode, String statusMessage, T data) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.data = data;
    }

    public static <T> ResponseEntity<CustomResponse<T>> success(HttpStatus status, T data) {
        CustomResponse<T> customResponse = new CustomResponse<T>(status.value(), "OK", data);
        return ResponseEntity.status(status).body(customResponse);
    }

    public static <T> ResponseEntity<CustomResponse<T>> download(T data) {
        CustomResponse<T> customResponse = new CustomResponse<T>(200, "OK", data);
        return ResponseEntity.status(HttpStatus.OK).body(customResponse);
    }
}
