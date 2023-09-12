package com.api.vetlens.controller;

import com.api.vetlens.dto.MessageDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisRequestDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisResponseDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisValidationDTO;
import com.api.vetlens.dto.questionary.QuestionDTO;
import com.api.vetlens.dto.questionary.QuestionaryDTO;
import com.api.vetlens.service.DiagnosisService;
import com.api.vetlens.service.QRService;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/diagnosis")
@Slf4j
@Tag(name = "Diagnosis Controller")
public class DiagnosisController {
    private final QRService qrService;
    private final DiagnosisService diagnosisService;

    @Operation(
            summary = "Obtener preguntas para comenzar el diagnostico",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
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
    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getQuestions() {
        log.info("Request /diagnosis/questions");
        return ResponseEntity.ok(diagnosisService.getQuestions());
    }

    @Operation(
            summary = "Comenzar el diagnóstico",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Dog not found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageDTO.class))
                    ),
                    @ApiResponse(
                            description = "El campo id del perro / preguntas no puede estar vacío",
                            responseCode = "400",
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
    @PostMapping("/start")
    public ResponseEntity<DiagnosisResponseDTO> startDiagnosis(@RequestBody @Valid DiagnosisRequestDTO request) {
        log.info("Request /diagnosis/start");
        return ResponseEntity.ok(diagnosisService.startDiagnosis(request));
    }

    @Operation(
            summary = "Concluir el diagnóstico",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Diagnóstico no existe",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageDTO.class))
                    ),
                    @ApiResponse(
                            description = "No se ha enviado una imagen / El campo 'id diagnostico' no puede estar vacío",
                            responseCode = "400",
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
    @PostMapping("/conclude/{diagnosisId}")
    public ResponseEntity<DiagnosisResponseDTO> concludeDiagnosis(@NotNull(message = "No se ha enviado una imagen") @RequestPart(name = "image") MultipartFile image, @NotNull(message = "El campo 'id diagnostico' no puede estar vacío") @PathVariable Integer diagnosisId) throws IOException, WriterException {
        log.info("Request /diagnosis/conclude/"+diagnosisId);
        return ResponseEntity.ok(diagnosisService.concludeDiagnosis(image, diagnosisId));
    }

    @Operation(
            summary = "Obtener preguntas del diagnóstico"
    )
    @GetMapping("/questions/{diagnosisId}")
    public ResponseEntity<QuestionaryDTO> getDiagnosisQuestions(@NotNull(message = "El campo 'id del diagnostico' no puede estar vacío") @PathVariable Integer diagnosisId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisQuestions(diagnosisId));
    }

    @Operation(
            summary = "Obtener diagnóstico por perro"
    )
    @GetMapping("/dog/{dogId}")
    public ResponseEntity<List<DiagnosisResponseDTO>> getDiagnosisByDog(@NotNull(message = "El campo 'id del perro' no puede estar vacío") @PathVariable Integer dogId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisByDog(dogId));
    }

    @Operation(
            summary = "Obtener diagnóstico por id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<DiagnosisResponseDTO> getDiagnosisById(@NotNull(message = "El campo 'id' no puede estar vacío") @PathVariable Integer id) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisById(id));
    }

    @Operation(
            summary = "Obtener validacion de diagnostico por veterinario"
    )
    @GetMapping("/{diagnosisId}/{username}")
    public ResponseEntity<DiagnosisValidationDTO> getDiagnosisValidation(@PathVariable Integer diagnosisId, @PathVariable String username) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisValidation(diagnosisId, username));
    }

    @Operation(
            summary = "Obtener validacion de diagnostico por veterinario y estado"
    )
    @GetMapping("/validation/{username}/{validationState}")
    public ResponseEntity<List<DiagnosisValidationDTO>> getDiagnosisValidationByVetAndValue(@PathVariable String username, @PathVariable String validationState) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisValidationsByVetAndValue(username, validationState));
    }

    @Operation(
            summary = "Obtener QR de un diagnóstico",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageDTO.class))
                    ),
                    @ApiResponse(
                            description = "Diagnóstico no existe",
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
    @GetMapping("/qr/{diagnosisId}")
    public ResponseEntity<String> getQr(@PathVariable Integer diagnosisId) {
        byte[] response = qrService.getQr(diagnosisId);
        String base64Response = Base64.encodeBase64String(response);
        return ResponseEntity.ok(base64Response);
    }

    @Operation(
            summary = "Obtener los diagnósticos realizados por un usuario en los últimos 7 días"
    )
    @GetMapping("/recent/{username}")
    public ResponseEntity<List<DiagnosisResponseDTO>> getRecentDiagnosisByUser(@PathVariable String username) {
        return ResponseEntity.ok(diagnosisService.getRecentDiagnosisByUser(username));
    }
}
