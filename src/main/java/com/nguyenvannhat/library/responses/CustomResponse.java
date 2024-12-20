package com.nguyenvannhat.library.responses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static <T> ResponseEntity<CustomResponse<T>> success(HttpStatus status, SuccessCode successCode, MessageSource messageSource, T data) {
        String message = messageSource.getMessage(successCode.getCode(), null, Locale.getDefault());
        CustomResponse<T> customResponse = new CustomResponse<>(successCode.getCode(), message, data);
        return ResponseEntity.status(status).body(customResponse);
    }

    public static ResponseEntity<ByteArrayResource> download(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName().toString())
                    .body(resource);
        } finally {
            Files.deleteIfExists(path);
        }
    }
}
