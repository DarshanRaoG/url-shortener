package com.darshanraog.urlshortener.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UrlMappingDTO(
        String originalUrl,
        String shortCode,
        LocalDateTime createdAt,
        LocalDateTime expiresAt,
        long clickCount
) {
}
