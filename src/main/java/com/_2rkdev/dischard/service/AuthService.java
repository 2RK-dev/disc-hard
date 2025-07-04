package com._2rkdev.dischard.service;

import com._2rkdev.dischard.dto.common.UserDTO;
import com._2rkdev.dischard.dto.rest.LoginRequestDTO;
import com._2rkdev.dischard.dto.rest.LoginResponseDTO;
import com._2rkdev.dischard.dto.rest.RegistrationRequestDTO;
import com._2rkdev.dischard.entity.User;
import com._2rkdev.dischard.exception.EmailTakenException;
import com._2rkdev.dischard.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public LoginResponseDTO register(@NotNull RegistrationRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailTakenException("The email is already taken, an user with the email is already registered");
        }
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(userDetailsService.loadUserByUsername(user.getEmail()));
        return new LoginResponseDTO(token, UserDTO.from(user));
    }

    public LoginResponseDTO login(@NotNull LoginRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String token = jwtService.generateToken(userDetailsService.loadUserByUsername(request.email()));
        return new LoginResponseDTO(token, UserDTO.from(userRepository.findByEmail(request.email()).orElseThrow()));
    }
}
