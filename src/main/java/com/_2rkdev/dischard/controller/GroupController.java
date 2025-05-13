package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.dto.rest.CreateGroupRequest;
import com._2rkdev.dischard.dto.rest.GroupDataResponse;
import com._2rkdev.dischard.dto.rest.GroupInfo;
import com._2rkdev.dischard.dto.rest.GroupInvitationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @PostMapping
    public ResponseEntity<GroupInfo> createGroup(@RequestBody CreateGroupRequest request) {
        throw new UnsupportedOperationException();
    }

    @DeleteMapping("/{groupId}/members/me")
    public ResponseEntity<Void> leaveGroup(@PathVariable String groupId) {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<Void> inviteToGroup(
            @PathVariable String groupId,
            @RequestBody GroupInvitationRequest request) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDataResponse> getGroupInfo(@PathVariable String groupId) {
        throw new UnsupportedOperationException();
    }
}
