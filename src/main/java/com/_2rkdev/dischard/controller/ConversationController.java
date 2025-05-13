package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.dto.common.Member;
import com._2rkdev.dischard.dto.rest.MessagePagedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    @GetMapping
    public ResponseEntity<List<?>> getConversations() {
        // The actual return type would be ConversationListResponse
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<MessagePagedResponse> getConversationMessages(
            @PathVariable String conversationId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{conversationId}/members")
    public ResponseEntity<List<Member>> getConversationMembers(@PathVariable String conversationId) {
        throw new UnsupportedOperationException();
    }
}
