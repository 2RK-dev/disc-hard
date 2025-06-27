package com._2rkdev.dischard.dto.rest;

import com._2rkdev.dischard.dto.common.Member;

public record PrivateConversationDTO(
        Long id,
        String type,
        Member contact
) {
}
