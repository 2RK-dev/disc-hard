package com._2rkdev.dischard.service;

import com._2rkdev.dischard.dto.common.UserDTO;
import com._2rkdev.dischard.dto.rest.ProfileUpdateRequestDTO;
import com._2rkdev.dischard.dto.rest.ProfileUpdateResponseDTO;
import com._2rkdev.dischard.entity.User;
import com._2rkdev.dischard.exception.UserNotFoundException;
import com._2rkdev.dischard.mapper.UserMapper;
import com._2rkdev.dischard.repository.UserRepository;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public ProfileUpdateResponseDTO updateProfile(String email, @Valid @NotNull ProfileUpdateRequestDTO request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        user.setName(request.name());
        userRepository.save(user);
        return new ProfileUpdateResponseDTO(user.getEmail(), user.getName());
    }

    public List<UserDTO> allUsers() {
        return userMapper.toUserDTOList(userRepository.findAll());
    }

    public UserDTO userById(Long userId) {
        return userMapper.toUserDTO(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found: " + userId)));
    }
}
