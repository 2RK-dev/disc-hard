package com._2rkdev.dischard.controller;

import com._2rkdev.dischard.dto.rest.LoginRequest;
import com._2rkdev.dischard.dto.rest.LoginResponse;
import com._2rkdev.dischard.dto.rest.RegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegistrationRequest request) {
        throw new UnsupportedOperationException();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        throw new UnsupportedOperationException();
    }
}
