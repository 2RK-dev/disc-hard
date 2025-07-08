package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.dto.common.MemberDTO;
import com._2rkdev.dischard.dto.rest.ConversationBaseDTO;
import com._2rkdev.dischard.dto.rest.MessagePagedResponseDTO;
import com._2rkdev.dischard.service.ConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping
    public ResponseEntity<List<? extends ConversationBaseDTO>> getConversations(@AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(conversationService.getConversations(principal.getUsername()));
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<MessagePagedResponseDTO> getConversationMessages(
            @PathVariable long conversationId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(conversationService.getConversationMessages(conversationId, principal.getUsername(), page, size));
    }

    @GetMapping("/{conversationId}/members")
    public ResponseEntity<List<MemberDTO>> getConversationMembers(
            @PathVariable long conversationId,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(conversationService.getConversationMembers(conversationId, principal.getUsername()));
    }
}
