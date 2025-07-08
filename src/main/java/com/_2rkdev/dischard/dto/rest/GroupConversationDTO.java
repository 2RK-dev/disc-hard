package com._2rkdev.dischard.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record GroupConversationDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("createdAt")
        OffsetDateTime createdAt
) implements ConversationBaseDTO {

    @Override
    @JsonProperty("type")
    public String type() {
        return "group";
    }
}