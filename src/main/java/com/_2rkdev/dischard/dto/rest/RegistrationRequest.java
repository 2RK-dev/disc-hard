package com._2rkdev.dischard.dto.rest;

public record RegistrationRequest(
        String email,
        String password,
        String passwordConfirm,
        String name
) {
}
