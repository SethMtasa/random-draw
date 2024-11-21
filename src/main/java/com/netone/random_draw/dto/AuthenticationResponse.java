package com.netone.random_draw.dto;

public record AuthenticationResponse<String>(boolean success, String message, String token) {
}
