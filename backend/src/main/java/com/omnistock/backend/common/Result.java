package com.omnistock.backend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 统一返回结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(200)
                .message("success")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> Result<T> success() {
        return Result.<T>builder()
                .code(200)
                .message("success")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> Result<T> created(T data) {
        return Result.<T>builder()
                .code(201)
                .message("created")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> Result<T> fail(int code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> Result<T> fail(int code, String message, T data) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
