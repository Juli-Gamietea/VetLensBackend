package com.api.vetlens.controller;

import com.api.vetlens.dto.MessageDTO;
import com.api.vetlens.dto.PredictionDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisRequestDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisResponseDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisValidationDTO;
import com.api.vetlens.dto.diagnosis.DiseaseDTO;
import com.api.vetlens.dto.questionary.QuestionDTO;
import com.api.vetlens.entity.Disease;
import com.api.vetlens.entity.Inference;
import com.api.vetlens.exceptions.ApiException;
import com.api.vetlens.service.DiagnosisService;
import com.api.vetlens.service.DiseaseService;
import com.api.vetlens.service.InferenceService;
import com.api.vetlens.service.QRService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/diagnosis")
@Tag(name = "Diagnosis Controller")
public class DiagnosisController {
    private final QRService qrService;
    private final DiagnosisService diagnosisService;

    private final DiseaseService diseaseService;

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
    public ResponseEntity<DiagnosisResponseDTO> concludeDiagnosis(@NotNull(message = "No se ha enviado una imagen") @RequestPart(name = "image") MultipartFile image, @NotNull(message = "El campo 'id diagnostico' no puede estar vacío") @PathVariable Integer diagnosisId) {
        return ResponseEntity.ok(diagnosisService.concludeDiagnosis(image, diagnosisId));
    }

    @Operation(
            summary = "Obtener diagnóstico por perro"
    )
    @GetMapping("/dog/{dogId}")
    public ResponseEntity<List<DiagnosisResponseDTO>> getDiagnosisByDog(@NotNull(message = "El campo 'id del perro' no puede estar vacío") @PathVariable Integer dogId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisByDog(dogId));
    }

    @Operation(
            summary = "Obtener validacion de diagnostico por veterinario"
    )
    @GetMapping("/{diagnosisId}/{userId}")
    public ResponseEntity<DiagnosisValidationDTO> getDiagnosisValidation(@PathVariable Integer diagnosisId, @PathVariable Integer userId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisValidation(diagnosisId, userId));
    }

    @Operation(
            summary = "Obtener validacion de diagnostico por veterinario y perro"
    )
    @GetMapping("/{vetId}/{value}")
    public ResponseEntity<List<DiagnosisValidationDTO>> getDiagnosisValidationByVetAndValue(@PathVariable Integer vetId, @PathVariable String value) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisValidationsByVetAndValue(vetId, value));
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
    public ResponseEntity<byte[]> getQr(@PathVariable Integer diagnosisId) {
        byte[] response = qrService.getQr(diagnosisId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(response.length)
                .header("Content-Disposition", "attachment; filename=qr.png")
                .body(response);
    }
}
