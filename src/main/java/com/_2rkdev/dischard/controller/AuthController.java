package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.dto.rest.LoginRequestDTO;
import com._2rkdev.dischard.dto.rest.LoginResponseDTO;
import com._2rkdev.dischard.dto.rest.RegistrationRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegistrationRequestDTO request) {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        throw new UnsupportedOperationException();
    }
}
