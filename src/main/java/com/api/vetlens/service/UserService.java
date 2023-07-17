package com.api.vetlens.service;

import com.api.vetlens.dto.UserRequestDTO;
import com.api.vetlens.dto.UserResponseDTO;
import com.api.vetlens.entity.Role;
import com.api.vetlens.entity.User;
import com.api.vetlens.exceptions.UserNotFoundException;
import com.api.vetlens.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private ModelMapper mapper = new ModelMapper();
    public UserResponseDTO update(UserRequestDTO request){
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setEmail(request.getEmail());
            if(request.getRole().equals("VET")){
                user.setRole(Role.VET);
            }
            user.setFirstName(request.getFirstName());
            user.setLastname(request.getLastname());
            user.setLicenceNumber(request.getLicenceNumber());
            User savedUser = userRepository.save(user);
            return mapper.map(savedUser, UserResponseDTO.class);
        }
        throw new UserNotFoundException("Usuario " + request.getUsername() + " not found");
    }
}
