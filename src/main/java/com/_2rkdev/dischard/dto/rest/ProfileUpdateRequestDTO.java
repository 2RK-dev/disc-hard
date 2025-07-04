package com._2rkdev.dischard.dto.rest;

import jakarta.validation.constraints.NotBlank;

public record ProfileUpdateRequestDTO(
        @NotBlank(message = "Name is required")
        String name
) {
}
