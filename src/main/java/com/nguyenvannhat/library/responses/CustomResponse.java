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
    private int code;          // Mã trạng thái HTTP (vd: 200, 400, 500)
    private String message;    // Thông báo
    private T data;            // Dữ liệu (Generic)

    // Constructor
    public CustomResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseEntity<CustomResponse<T>> success(T data) {
        CustomResponse<T> response = new CustomResponse<>(HttpStatus.OK, "Success", data);
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<CustomResponse<Void>> error(ResponseStatus status, String message) {
        CustomResponse<Void> response = new CustomResponse<>(status.code(), message, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public static ResponseEntity<byte[]> download(File file) {
        // Kiểm tra nếu tệp không tồn tại
        if (file == null || !file.exists()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(null);
        }

        try {
            // Chuyển file thành byte array
            Path filePath = Paths.get(file.getAbsolutePath());
            byte[] fileBytes = Files.readAllBytes(filePath);

            // Thiết lập headers cho tải file
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            // Trả về ResponseEntity với nội dung file
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);

        } catch (IOException e) {
            // Xử lý trường hợp lỗi khi đọc file
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(null);
        }
    }
}
