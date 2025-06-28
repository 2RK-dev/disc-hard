package com._2rkdev.dischard.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PasswordChangeRequestDTO(
        String old,
        @JsonProperty("new") String new_,
        String confirm
) {
}
