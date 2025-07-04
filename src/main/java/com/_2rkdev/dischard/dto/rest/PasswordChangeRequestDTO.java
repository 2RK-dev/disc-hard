package com._2rkdev.dischard.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequestDTO(
        @NotBlank(message = "Old password is required")
        String old,
        @NotBlank(message = "New password is required")
        @JsonProperty("new") String new_,
        @NotBlank(message = "Confirm password is required")
        String confirm
) {
}
