package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.dto.rest.LoginRequestDTO;
import com._2rkdev.dischard.dto.rest.LoginResponseDTO;
import com._2rkdev.dischard.dto.rest.RegistrationRequestDTO;
import com._2rkdev.dischard.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> registerUser(@Valid @RequestBody RegistrationRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
