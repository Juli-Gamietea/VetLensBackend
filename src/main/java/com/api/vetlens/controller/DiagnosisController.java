package com.api.vetlens.controller;

import com.api.vetlens.dto.MessageDTO;
import com.api.vetlens.dto.PredictionDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisRequestDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisResponseDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisValidationDTO;
import com.api.vetlens.dto.questionary.QuestionDTO;
import com.api.vetlens.service.DiagnosisService;
import com.api.vetlens.service.InferenceService;
import com.api.vetlens.service.QRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diagnosis")
public class DiagnosisController {
    private QRService qrService;
    private final DiagnosisService diagnosisService;
    private final InferenceService inferenceService;

    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getQuestions() {
        return ResponseEntity.ok(diagnosisService.getQuestions());
    }

    @PostMapping("/start")
    public ResponseEntity<DiagnosisResponseDTO> startDiagnosis(@RequestBody DiagnosisRequestDTO request) {
        return ResponseEntity.ok(diagnosisService.startDiagnosis(request));
    }

    @PostMapping("/conclude/{diagnosisId}")
    public ResponseEntity<DiagnosisResponseDTO> concludeDiagnosis(@RequestPart(name = "image") MultipartFile image, @PathVariable Integer diagnosisId) {
        return ResponseEntity.ok(diagnosisService.concludeDiagnosis(image, diagnosisId));
    }

    @GetMapping("/dog/{dogId}")
    public ResponseEntity<List<DiagnosisResponseDTO>> getDiagnosisByDog(@PathVariable Integer dogId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisByDog(dogId));
    }

    @GetMapping("/{diagnosisId}/{userId}")
    public ResponseEntity<DiagnosisValidationDTO> getDiagnosisValidation(@PathVariable Integer diagnosisId, @PathVariable Integer userId) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisValidation(diagnosisId, userId));
    }

    @GetMapping("/{vetId}/{value}")
    public ResponseEntity<List<DiagnosisValidationDTO>> getDiagnosisValidationByVetAndValue(@PathVariable Integer vetId, @PathVariable String value) {
        return ResponseEntity.ok(diagnosisService.getDiagnosisValidationsByVetAndValue(vetId, value));
    }
    @PostMapping("/test")
    public ResponseEntity<PredictionDTO> test(@RequestPart(name = "image") MultipartFile image) {
        return ResponseEntity.ok(inferenceService.makeInference(image));
    }

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
