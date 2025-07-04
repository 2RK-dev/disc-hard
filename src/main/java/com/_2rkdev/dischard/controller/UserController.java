package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.dto.common.UserDTO;
import com._2rkdev.dischard.dto.rest.PasswordChangeRequestDTO;
import com._2rkdev.dischard.dto.rest.ProfileUpdateRequestDTO;
import com._2rkdev.dischard.dto.rest.ProfileUpdateResponseDTO;
import com._2rkdev.dischard.service.AuthService;
import com._2rkdev.dischard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PatchMapping("/profile")
    public ResponseEntity<ProfileUpdateResponseDTO> updateProfile(@Valid @RequestBody ProfileUpdateRequestDTO request, @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(userService.updateProfile(principal.getUsername(), request));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequestDTO request, @AuthenticationPrincipal UserDetails principal) {
        authService.changePassword(principal.getUsername(), request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> listUsers(
            @RequestParam(required = false) String nameFilter,
            @RequestParam(required = false) String emailFilter) {
        return ResponseEntity.ok(userService.allUsers());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.userById(userId));
    }
}
