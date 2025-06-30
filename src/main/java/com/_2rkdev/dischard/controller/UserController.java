package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.dto.common.UserDTO;
import com._2rkdev.dischard.dto.rest.PasswordChangeRequestDTO;
import com._2rkdev.dischard.dto.rest.ProfileUpdateRequestDTO;
import com._2rkdev.dischard.dto.rest.ProfileUpdateResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @PatchMapping("/profile")
    public ResponseEntity<ProfileUpdateResponseDTO> updateProfile(@Valid @RequestBody ProfileUpdateRequestDTO request) {
        throw new UnsupportedOperationException();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequestDTO request) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> listUsers(
            @RequestParam(required = false) String nameFilter,
            @RequestParam(required = false) String emailFilter) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable String userId) {
        throw new UnsupportedOperationException();
    }
}
