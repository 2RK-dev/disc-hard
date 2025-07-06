package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.dto.rest.CreateGroupRequestDTO;
import com._2rkdev.dischard.dto.rest.GroupDataResponseDTO;
import com._2rkdev.dischard.dto.rest.GroupInfoDTO;
import com._2rkdev.dischard.dto.rest.GroupInvitationRequestDTO;
import com._2rkdev.dischard.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<GroupInfoDTO> createGroup(
            @Valid @RequestBody CreateGroupRequestDTO request,
            @AuthenticationPrincipal UserDetails principal) {
        GroupInfoDTO group = groupService.createGroup(request, principal.getUsername());
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @DeleteMapping("/{groupId}/members/me")
    public ResponseEntity<Void> leaveGroup(
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserDetails principal) {
        groupService.leaveGroup(groupId, principal.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<Void> inviteToGroup(
            @PathVariable Long groupId,
            @Valid @RequestBody GroupInvitationRequestDTO request,
            @AuthenticationPrincipal UserDetails principal) {
        groupService.inviteToGroup(groupId, request, principal.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDataResponseDTO> getGroupInfo(@PathVariable Long groupId, @AuthenticationPrincipal UserDetails principal) {
        GroupDataResponseDTO groupData = groupService.getGroupInfo(groupId, principal.getUsername());
        return ResponseEntity.ok(groupData);
    }
}
