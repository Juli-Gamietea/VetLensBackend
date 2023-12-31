package com.api.vetlens.controller;

import com.api.vetlens.dto.MessageDTO;
import com.api.vetlens.dto.authentication.AuthenticationRequestDTO;
import com.api.vetlens.dto.authentication.AuthenticationResponseDTO;
import com.api.vetlens.dto.user.UserRequestDTO;
import com.api.vetlens.service.AuthenticationService;
import com.api.vetlens.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Operation(
            summary = "Registrar usuario",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "El nombre de usuario se encuentra en uso",
                            responseCode = "500",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageDTO.class))
                    ),
                    @ApiResponse(
                            description = "Internal server error",
                            responseCode = "500",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageDTO.class))
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(
            summary = "Iniciar sesión",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Usuario no encontrado",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageDTO.class))
                    ),
                    @ApiResponse(
                            description = "Forbidden",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageDTO.class))
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequest) {
        return ResponseEntity.ok(authenticationService.login(authenticationRequest));
    }

    @Operation(
            summary = "Obtener token nuevo a partir del refresh token"
    )
    @PostMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refresh(request, response);
    }

    @Operation(
            summary = "Comprobar disponibilidad de nombre de usuario"
    )
    @GetMapping("/available/{username}")
    public boolean checkUsernameAvailability(@PathVariable String username) {
        return userService.checkUsernameAvailability(username);
    }

    @Operation(
            summary = "Restaurar contraseña"
    )
    @PutMapping("/password/restore/{username}")
    public ResponseEntity<MessageDTO> forgotPassword(@PathVariable String username) {
        return ResponseEntity.ok(userService.forgotPassword(username));
    }
    @PutMapping("/student/{username}/file")
    public ResponseEntity<MessageDTO> addUserFile(@RequestPart(name = "file") MultipartFile file, @PathVariable String username) {
        return ResponseEntity.ok(userService.uploadFile(file, username));
    }
}
