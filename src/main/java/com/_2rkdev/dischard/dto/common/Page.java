package com._2rkdev.dischard.dto.common;

public record PageDTO(
        int size,
        int totalElements,
        int totalPages,
        int number
) {
}