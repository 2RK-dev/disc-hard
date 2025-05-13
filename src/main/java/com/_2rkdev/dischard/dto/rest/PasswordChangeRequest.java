package com._2rkdev.dischard.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PasswordChangeRequest(
        String old,
        @JsonProperty("new") String new_,
        String confirm
) {
}
