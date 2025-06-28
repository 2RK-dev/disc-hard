package com._2rkdev.dischard.dto.rest;

import com._2rkdev.dischard.dto.common.MemberDTO;

public record PrivateConversationDTO(
        Long id,
        String type,
        MemberDTO contact
) {
}
