package com._2rkdev.dischard.dto.rest;

import com._2rkdev.dischard.dto.common.Message;
import com._2rkdev.dischard.dto.common.Page;

import java.util.List;

public record MessagePagedResponse(
        List<Message> messages,
        Page page
) {
}
