package com._2rkdev.dischard.dto.rest;

public record RegistrationRequestDTO(
        String email,
        String password,
        String passwordConfirm,
        String name
) {
}
