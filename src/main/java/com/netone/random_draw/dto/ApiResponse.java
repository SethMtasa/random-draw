package com.netone.random_draw.dto;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(boolean success, String message, T body, HttpStatus status, String errorCode) {
    public ApiResponse(boolean success, String message, T body, HttpStatus status) {
        this(success, message, body, status, null);
    }
    public ApiResponse(boolean success, String message, T body) {
        this(success, message, body, HttpStatus.OK, null);
    }
}