package com._2rkdev.dischard.dto.rest;

import com._2rkdev.dischard.dto.common.MemberDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PrivateConversationDTO(
        @JsonProperty("id") Long id,
        @JsonProperty("contact") MemberDTO contact
) implements ConversationBaseDTO {

    @Override
    @JsonProperty("type")
    public String type() {
        return "private";
    }
}