package com.api.vetlens.service;

import com.api.vetlens.dto.diagnosis.DiagnosisRequestDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisResponseDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisValidationDTO;
import com.api.vetlens.dto.questionary.QuestionDTO;
import com.api.vetlens.entity.*;
import com.api.vetlens.entity.questionary.Question;
import com.api.vetlens.entity.questionary.Questionary;
import com.api.vetlens.exceptions.ApiException;
import com.api.vetlens.exceptions.NotFoundException;
import com.api.vetlens.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DiagnosisService {
    private final InferenceService inferenceService;
    private final CloudinaryService cloudinaryService;
    private final DiagnosisRepository diagnosisRepository;
    private final QuestionaryRepository questionaryRepository;
    private final QuestionRepository questionRepository;
    private final AnamnesisRepository anamnesisRepository;
    private final DogRepository dogRepository;
    private final DiagnosisValidationRepository diagnosisValidationRepository;
    private final UserService userService;
    private final ModelMapper mapper = new ModelMapper();

    public DiagnosisResponseDTO startDiagnosis(DiagnosisRequestDTO request) {
        Diagnosis diagnosis = new Diagnosis();
        Anamnesis anamnesis = new Anamnesis();
        LocalDate date = LocalDate.now();
        Optional<Dog> optionalDog = dogRepository.findById(request.getDogId());
        if (optionalDog.isEmpty()) {
            throw new NotFoundException("El perro no existe");
        }
        Dog dog = optionalDog.get();

        List<Question> questions = mapQuestionsToEntities(request.getQuestions());
        String questionaryId = request.getUsername() + "|" + date + "|" + dog.getName() + "(" + UUID.randomUUID() + ")";
        questionaryRepository.save(new Questionary(questionaryId, questions));
        anamnesis.setQuestionaryId(questionaryId);
        anamnesis.setInferences(new ArrayList<>());

        diagnosis.setAnamnesis(anamnesis);
        diagnosis.setDate(date);
        diagnosis.setDog(dog);
        return mapper.map(diagnosisRepository.save(diagnosis), DiagnosisResponseDTO.class);
    }

    public DiagnosisResponseDTO concludeDiagnosis(MultipartFile image, Integer diagnosisId) {
        Optional<Diagnosis> diagnosisOptional = diagnosisRepository.findById(diagnosisId);
        if (diagnosisOptional.isEmpty()) {
            throw new NotFoundException("El diagnóstico no existe");
        }
        Diagnosis diagnosis = diagnosisOptional.get();
        String imageUrl = cloudinaryService.uploadDiagnosisFile(image, diagnosis.getDog().getOwner().getUsername(), diagnosis.getDog().getName());
        diagnosis.setImageUrl(imageUrl);

        Anamnesis anamnesis = diagnosis.getAnamnesis();
        anamnesis = inferenceService.makeInference(image, anamnesis);
        anamnesisRepository.save(anamnesis);
        diagnosis.setAnamnesis(anamnesis);

        DiagnosisResponseDTO response = mapper.map(diagnosisRepository.save(diagnosis), DiagnosisResponseDTO.class);
        return response;
    }

    public DiagnosisValidationDTO getDiagnosisValidation(Integer diagnosisId, Integer userId) {
        Optional<Diagnosis> diagnosisOptional = diagnosisRepository.findById(diagnosisId);
        if(diagnosisOptional.isEmpty()){
            throw new NotFoundException("Diagnóstico no encontrado");
        }
        User user = userService.getUserById(userId);
        Optional<DiagnosisValidation> diagnosisValidationOptional = diagnosisValidationRepository.findByVet_IdAndDiagnosis_Id(userId, diagnosisId);
        if (diagnosisValidationOptional.isEmpty()) {
            DiagnosisValidation validation = DiagnosisValidation.builder()
                    .vet(user)
                    .diagnosis(diagnosisOptional.get())
                    .notes(null)
                    .disease(null)
                    .value(Value.NOT_VALIDATED)
                    .build();
            return mapper.map(diagnosisValidationRepository.save(validation), DiagnosisValidationDTO.class);
        }
        return mapper.map(diagnosisValidationOptional.get(), DiagnosisValidationDTO.class);
    }

    public DiagnosisResponseDTO getDiagnosisById(Integer id) {
        Optional<Diagnosis> diagnosisOptional = diagnosisRepository.findById(id);
        if (diagnosisOptional.isEmpty()) {
            throw new NotFoundException("El diagnóstico no existe");
        }
        return mapper.map(diagnosisOptional.get(), DiagnosisResponseDTO.class);
    }

    public List<DiagnosisResponseDTO> getDiagnosisByDog(Integer dogId) {
        List<Diagnosis> diagnosisList = diagnosisRepository.findAllByDog_Id(dogId);
        if (diagnosisList.isEmpty()) {
            throw new NotFoundException("El perro no posee diagnósticos");
        }
        return diagnosisList.stream().map(
                diagnosis -> mapper.map(diagnosis, DiagnosisResponseDTO.class)
        ).collect(Collectors.toList());
    }

    public List<DiagnosisValidationDTO> getDiagnosisValidationsByVetAndValue(Integer userId, String value){
        List<DiagnosisValidation> validationsList;
        if(value == null){
            validationsList = diagnosisValidationRepository.findAllByVet_Id(userId);
        }else{
            Value valueEnum = Value.NOT_VALIDATED;
            switch (value){
                case "CORRECT":
                    valueEnum = Value.CORRECT;
                    break;
                case "INCORRECT":
                    valueEnum = Value.INCORRECT;
                    break;
            }
            validationsList = diagnosisValidationRepository.findAllByVet_IdAndValue(userId, valueEnum);
        }

        if (validationsList.isEmpty()) {
            throw new NotFoundException("El veterinario no posee diagnósticos");
        }
        return validationsList.stream().map(
                validation -> mapper.map(validation, DiagnosisValidationDTO.class)
        ).collect(Collectors.toList());
    }

    public List<QuestionDTO> getQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream().map(
                question -> mapper.map(question, QuestionDTO.class)
        ).collect(Collectors.toList());
    }

    private List<Question> mapQuestionsToEntities(List<QuestionDTO> questionDTOS) {
        return questionDTOS.stream().map(
                questionDTO -> mapper.map(questionDTO, Question.class)
        ).collect(Collectors.toList());
    }

}
