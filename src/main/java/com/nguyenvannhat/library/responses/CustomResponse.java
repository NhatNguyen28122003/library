package com.nguyenvannhat.library.responses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    // Trả về thành công với dữ liệu
    public static <T> ResponseEntity<CustomResponse<T>> success(T data) {
        CustomResponse<T> response = new CustomResponse<>(HttpStatus.OK, "Success", data);
        return ResponseEntity.ok(response);
    }

    // Trả về lỗi không có dữ liệu
    public static ResponseEntity<CustomResponse<Void>> error(ResponseStatus status, String message) {
        CustomResponse<Void> response = new CustomResponse<>(status.code(), message, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
