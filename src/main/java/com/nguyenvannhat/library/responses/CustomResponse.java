package com.nguyenvannhat.library.responses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Setter
@Getter
public class CustomResponse<T> {
    private int code;
    private String message;
    private T data;

    public CustomResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseEntity<?> success(T data) {
        CustomResponse<T> response = new CustomResponse<>(HttpStatus.OK, "Success", data);
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<?> error(ResponseStatus status, String message) {
        CustomResponse<Void> response = new CustomResponse<>(status.code(), message, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public static ResponseEntity<?> download(File file) {
        if (file == null || !file.exists()) {
            CustomResponse<Void> response = new CustomResponse<>(HttpStatus.NOT_FOUND,"Can't find file!!!",null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
            Path filePath = Paths.get(file.getAbsolutePath());
            byte[] fileBytes = Files.readAllBytes(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(null);
        }
    }
}
