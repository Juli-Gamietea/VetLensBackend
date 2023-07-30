package com.api.vetlens.controller;

import com.api.vetlens.dto.diagnosis.DiagnosisRequestDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisResponseDTO;
import com.api.vetlens.dto.questionary.QuestionDTO;
import com.api.vetlens.service.DiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diagnosis")
public class DiagnosisController {
    private final DiagnosisService diagnosisService;

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
}
