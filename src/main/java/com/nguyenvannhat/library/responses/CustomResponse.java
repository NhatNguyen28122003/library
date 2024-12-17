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

    // Cập nhật phương thức download để trả về file
    public static ResponseEntity<CustomResponse<Void>> download(ResponseStatus status, String message, File file) {
        // Kiểm tra nếu tệp không tồn tại
        if (file == null || !file.exists()) {
            CustomResponse<Void> response = new CustomResponse<>(HttpStatus.NOT_FOUND, "File not found", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Đọc tệp tin và trả về trong ResponseEntity
        try {
            // Chuyển file thành byte array
            Path filePath = Paths.get(file.getAbsolutePath());
            byte[] fileBytes = Files.readAllBytes(filePath);

            // Thiết lập headers cho tải file
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

            // Trả về ResponseEntity với nội dung tệp tin và headers
            CustomResponse<Void> response = new CustomResponse<>(status.code(), message, null);  // Trả về thông tin về tệp tin
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers)
                    .body(response);  // Chỉ trả về thông tin phản hồi, còn nội dung tệp tin sẽ được tải xuống qua headers

        } catch (IOException e) {
            // Xử lý trường hợp lỗi khi đọc file
            CustomResponse<Void> response = new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to download file", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
