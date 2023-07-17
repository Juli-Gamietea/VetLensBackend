package com.api.vetlens.service;

import com.api.vetlens.dto.MessageDTO;
import com.api.vetlens.dto.UserRequestDTO;
import com.api.vetlens.dto.UserResponseDTO;
import com.api.vetlens.entity.Role;
import com.api.vetlens.entity.User;
import com.api.vetlens.exceptions.UserNotFoundException;
import com.api.vetlens.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private ModelMapper mapper = new ModelMapper();

    public UserResponseDTO update(UserRequestDTO request) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(request.getEmail());
            if (request.getRole().equals("VET")) {
                user.setRole(Role.VET);
            }
            user.setFirstName(request.getFirstName());
            user.setLastname(request.getLastname());
            user.setLicenceNumber(request.getLicenceNumber());
            User savedUser = userRepository.save(user);
            return mapper.map(savedUser, UserResponseDTO.class);
        }
        throw new UserNotFoundException("Usuario " + request.getUsername() + " no encontrado");
    }

    public MessageDTO changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Usuario " + username + " no encontrado");
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UserNotFoundException("Contraseña incorrecta");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new MessageDTO("Contraseña actualizada exitosamente");
    }
}
