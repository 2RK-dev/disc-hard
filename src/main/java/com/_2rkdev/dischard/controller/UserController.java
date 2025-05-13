package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.dto.common.User;
import com._2rkdev.dischard.dto.rest.PasswordChangeRequest;
import com._2rkdev.dischard.dto.rest.ProfileUpdateRequest;
import com._2rkdev.dischard.dto.rest.ProfileUpdateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @PatchMapping("/profile")
    public ResponseEntity<ProfileUpdateResponse> updateProfile(@RequestBody ProfileUpdateRequest request) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeRequest request) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers(
            @RequestParam(required = false) String nameFilter,
            @RequestParam(required = false) String emailFilter) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserInfo(@PathVariable String userId) {
        throw new UnsupportedOperationException();
    }
}
