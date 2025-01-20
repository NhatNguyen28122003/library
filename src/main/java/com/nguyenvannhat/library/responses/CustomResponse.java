package com.nguyenvannhat.library.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
@AllArgsConstructor
public class CustomResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public static <T> CustomResponse<T> success(int statusCode, String message, T data) {
        return new CustomResponse<>(statusCode, message, data);
    }

    public static <T> CustomResponse<T> error(int statusCode, String message) {
        return new CustomResponse<>(statusCode, message, null);
    }

    public static <T extends File> CustomResponse<T> download(int statusCode, String message, T data) {
        return new CustomResponse<>(statusCode, message, data);
    }

}
