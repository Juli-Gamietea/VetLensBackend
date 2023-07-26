package com.api.vetlens.controller;

import com.api.vetlens.dto.DiagnosisRequestDTO;
import com.api.vetlens.dto.MessageDTO;
import com.api.vetlens.dto.questionary.QuestionDTO;
import com.api.vetlens.service.DiagnosisService;
import com.api.vetlens.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diagnosis")
public class DiagnosisController {
    private final DocumentService documentsService;
    private final DiagnosisService diagnosisService;
    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getQuestions(){
        return ResponseEntity.ok(documentsService.getQuestions());
    }

    @PostMapping("/make")
    public ResponseEntity<MessageDTO> makeDiagnosis(@RequestBody DiagnosisRequestDTO request){
        return ResponseEntity.ok(diagnosisService.makeDiagnosis(request));
    }
}
