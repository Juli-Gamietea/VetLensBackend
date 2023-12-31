package com.api.vetlens.controller;

import com.api.vetlens.dto.*;
import com.api.vetlens.dto.dog.DogRequestDTO;
import com.api.vetlens.dto.dog.DogResponseDTO;
import com.api.vetlens.dto.user.UserRequestDTO;
import com.api.vetlens.dto.user.UserResponseDTO;
import com.api.vetlens.dto.user.UserUpdateRequestDTO;
import com.api.vetlens.service.S3Service;
import com.api.vetlens.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = "User Controller")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Operation(
            summary = "Actualizar usuario",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageDTO.class))
                    ),
                    @ApiResponse(
                            description = "Usuario no encontrado",
                            responseCode = "404",
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
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid token",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageDTO.class))
                    )
            }
    )
    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody @Valid UserUpdateRequestDTO request) {
        return ResponseEntity.ok(userService.update(request));
    }

    @Operation(
            summary = "Obtener un usuario por su nombre de usuario"
    )
    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @Operation(
            summary = "Actualizar contraseña"
    )
    @PutMapping("/password/{username}/{oldPassword}/{newPassword}")
    public ResponseEntity<MessageDTO> changePassword(@PathVariable String username, @PathVariable String oldPassword, @PathVariable String newPassword) {
        return ResponseEntity.ok(userService.changePassword(username, oldPassword, newPassword));
    }
    @Operation(
            summary = "Agregar perro"
    )
    @PostMapping("/dog/add")
    public ResponseEntity<DogResponseDTO> addDog(@RequestBody @Valid DogRequestDTO request) {
        log.info("Request recibida en /dog/add");
        return ResponseEntity.ok(userService.addDog(request));
    }

    @Operation(
            summary = "Actualizar perro"
    )
    @PutMapping("/dog/update")
    public ResponseEntity<DogResponseDTO> updateDog(@RequestBody @Valid DogRequestDTO request) {
        return ResponseEntity.ok(userService.updateDog(request));
    }

    @Operation(
            summary = "Obtener perros de un usuario"
    )
    @GetMapping("/dogs/{username}")
    public ResponseEntity<List<DogResponseDTO>> getDogs(@PathVariable String username) {
        return ResponseEntity.ok(userService.getAllDogs(username));
    }

    @Operation(
            summary = "Agregar foto al perro"
    )
    @PutMapping("/dog/photo/{idDog}")
    public ResponseEntity<MessageDTO> addDogPhoto(@RequestPart(name = "image") MultipartFile image, @PathVariable Integer idDog) {
        return ResponseEntity.ok(userService.addDogPhoto(idDog, image));
    }

    @Operation(
            summary = "Eliminar foto del perro"
    )
    @PutMapping("/dog/photo/remove/{idDog}")
    public ResponseEntity<MessageDTO> removeDogPhoto(@PathVariable Integer idDog) {
        return ResponseEntity.ok(userService.removeDogPhoto(idDog));
    }

    @Operation(
            summary = "Eliminar perro"
    )
    @DeleteMapping("/dog/remove/{idDog}")
    public ResponseEntity<MessageDTO> removeDog(@PathVariable Integer idDog) {
        return ResponseEntity.ok(userService.removeDog(idDog));
    }
}
