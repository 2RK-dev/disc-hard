package com._2rkdev.dischard.dto.common;

public record Page(
        int size,
        int totalElements,
        int totalPages,
        int number
) {
}