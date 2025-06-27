package com._2rkdev.dischard.dto.rest;

import com._2rkdev.dischard.dto.common.Member;

import java.util.List;

public record GroupDataResponseDTO(
        GroupInfo info,
        List<Member> members
) {
}
