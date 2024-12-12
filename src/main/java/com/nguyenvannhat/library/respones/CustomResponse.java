package com.nguyenvannhat.library.respones;



import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Setter
@Getter
public class CustomResponse<T> {
    // Getter và Setter
    private int code;          // Mã trạng thái HTTP (vd: 400, 500)
    private String message;    // Thông báo lỗi
    private T data;            // Dữ liệu (Generic)

    // Constructor
    public CustomResponse(HttpStatus status, String message, T data) {
        this.code = status.value(); // Lấy mã trạng thái HTTP
        this.message = message;
        this.data = data;
    }

}

