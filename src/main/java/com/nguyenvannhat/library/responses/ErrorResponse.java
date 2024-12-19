package com.nguyenvannhat.library.responses;

import com.nguyenvannhat.library.exceptions.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;

@Getter
@Setter
public class ErrorResponse {
    private String errorCode;
    private String message;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
