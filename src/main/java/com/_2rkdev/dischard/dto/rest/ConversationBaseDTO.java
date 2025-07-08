package com._2rkdev.dischard.dto.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PrivateConversationDTO.class, name = "private"),
        @JsonSubTypes.Type(value = GroupConversationDTO.class, name = "group")
})
public sealed interface ConversationBaseDTO permits PrivateConversationDTO, GroupConversationDTO {
    @JsonProperty("id")
    Long id();

    @JsonProperty("type")
    String type();
}