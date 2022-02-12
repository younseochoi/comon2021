package com.cokung.comon.response.success;

import com.sun.istack.Nullable;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class SuccessResponse<T> {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private String code;
    private final String message;
    private T data;

    public static ResponseEntity<SuccessResponse> toResponseEntity(SuccessCode successCode, @Nullable Object data) {
        return ResponseEntity
                .status(successCode.getHttpStatus())
                .body(SuccessResponse.builder()
                        .status(successCode.getHttpStatus().value())
                        .code(successCode.getHttpStatus().name())
                        .message(successCode.getDetail())
                        .data(data)
                        .build()
                );
    }

    public static ResponseEntity<SuccessResponse> toResponseEntity(SuccessCode successCode) {
        return toResponseEntity(successCode, "no data");
    }
}
