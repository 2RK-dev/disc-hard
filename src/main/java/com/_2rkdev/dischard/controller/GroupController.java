package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.dto.rest.CreateGroupRequestDTO;
import com._2rkdev.dischard.dto.rest.GroupDataResponseDTO;
import com._2rkdev.dischard.dto.rest.GroupInfoDTO;
import com._2rkdev.dischard.dto.rest.GroupInvitationRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @PostMapping
    public ResponseEntity<GroupInfoDTO> createGroup(@Valid @RequestBody CreateGroupRequestDTO request) {
        throw new UnsupportedOperationException();
    }

    @DeleteMapping("/{groupId}/members/me")
    public ResponseEntity<Void> leaveGroup(@PathVariable String groupId) {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<Void> inviteToGroup(
            @PathVariable String groupId,
            @Valid @RequestBody GroupInvitationRequestDTO request) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDataResponseDTO> getGroupInfo(@PathVariable String groupId) {
        throw new UnsupportedOperationException();
    }
}
