package com.api.vetlens.controller;

import com.api.vetlens.dto.UserRequestDTO;
import com.api.vetlens.dto.UserResponseDTO;
import com.api.vetlens.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody @Valid UserRequestDTO request){
        return ResponseEntity.ok(userService.update(request));
    }
}
