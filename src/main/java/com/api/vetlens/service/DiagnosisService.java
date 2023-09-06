package com.api.vetlens.service;

import com.api.vetlens.dto.diagnosis.DiagnosisRequestDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisResponseDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisValidationDTO;
import com.api.vetlens.dto.questionary.QuestionDTO;
import com.api.vetlens.dto.questionary.QuestionaryDTO;
import com.api.vetlens.entity.*;
import com.api.vetlens.entity.questionary.Question;
import com.api.vetlens.entity.questionary.Questionary;
import com.api.vetlens.exceptions.NotFoundException;
import com.api.vetlens.repository.*;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
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
    private final QRService qrService;
    private final ModelMapper mapper = new ModelMapper();

    public DiagnosisResponseDTO startDiagnosis(DiagnosisRequestDTO request) {
        log.info("Comenzando el diagnostico...");
        Diagnosis diagnosis = new Diagnosis();
        Anamnesis anamnesis = new Anamnesis();
        LocalDate date = LocalDate.now();
        Optional<Dog> optionalDog = dogRepository.findById(request.getDogId());
        if (optionalDog.isEmpty()) {
            throw new NotFoundException("El perro no existe");
        }
        Dog dog = optionalDog.get();
        log.info("Buscando el perro");
        List<Question> questions = mapQuestionsToEntities(request.getQuestions());
        String questionaryId = request.getUsername() + "|" + date + "|" + dog.getName() + "(" + UUID.randomUUID() + ")";
        questionaryRepository.save(new Questionary(questionaryId, questions));
        log.info("Guardando cuestionario completado en MongoDB");
        anamnesis.setQuestionaryId(questionaryId);
        anamnesis.setInferences(new ArrayList<>());
        diagnosis.setAnamnesis(anamnesis);
        diagnosis.setDate(date);
        diagnosis.setDog(dog);
        log.info("Seteando perro y cuestionario al diagnostico iniciado");
        log.info("Guardando diagnostico iniciado");
        return mapper.map(diagnosisRepository.save(diagnosis), DiagnosisResponseDTO.class);
    }

    public DiagnosisResponseDTO concludeDiagnosis(MultipartFile image, Integer diagnosisId) throws IOException, WriterException {
        Optional<Diagnosis> diagnosisOptional = diagnosisRepository.findById(diagnosisId);
        log.info("Buscando el diagnostico con ID " + diagnosisId );
        if (diagnosisOptional.isEmpty()) {
            throw new NotFoundException("El diagnóstico no existe");
        }
        Diagnosis diagnosis = diagnosisOptional.get();

        Anamnesis anamnesis = diagnosis.getAnamnesis();
        anamnesis = inferenceService.makeInference(image, anamnesis);
        log.info("Inferencia completada");
        anamnesisRepository.save(anamnesis);
        diagnosis.setAnamnesis(anamnesis);

        String imageUrl = cloudinaryService.uploadDiagnosisFile(image, diagnosis.getDog().getOwner().getUsername(), diagnosis.getDog().getName());
        log.info("Subiendo imagen a Cloudinary");
        diagnosis.setImageUrl(imageUrl);

        log.info("Generando QR y enviandolo al mail del usuario...");
        qrService.generateQR(diagnosis, true);

        log.info("Proceso finalizado, devolviendo diagnostico completo...");
        return mapper.map(diagnosisRepository.save(diagnosis), DiagnosisResponseDTO.class);
    }

    public DiagnosisValidationDTO getDiagnosisValidation(Integer diagnosisId, String username) {
        Optional<Diagnosis> diagnosisOptional = diagnosisRepository.findById(diagnosisId);
        if(diagnosisOptional.isEmpty()){
            throw new NotFoundException("Diagnóstico no encontrado");
        }
        User user = userService.getUser(username);
        Optional<DiagnosisValidation> diagnosisValidationOptional = diagnosisValidationRepository.findByVet_IdAndDiagnosis_Id(user.getId(), diagnosisId);
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

    public List<DiagnosisResponseDTO> getRecentDiagnosisByUser (String username) {
        List<Dog> dogs = userService.getDogsList(username);
        List<Diagnosis> returnDiganosis = new ArrayList<>();
        for (Dog dog : dogs) {
            List<Diagnosis> diagnosis = diagnosisRepository.findAllByDog_IdAndDateBetween(dog.getId(), LocalDate.now().minusDays(7), LocalDate.now());
            returnDiganosis.addAll(diagnosis);
        }
        return returnDiganosis.stream().map(
                diagnosis -> mapper.map(diagnosis, DiagnosisResponseDTO.class)
        ).collect(Collectors.toList());
    }

    public List<DiagnosisValidationDTO> getDiagnosisValidationsByVetAndValue(String username, String value){
        List<DiagnosisValidation> validationsList;
        User vet = userService.getUser(username);

        if(value == null){
            validationsList = diagnosisValidationRepository.findAllByVet_Id(vet.getId());
        } else {
            Value valueEnum = switch (value) {
                case "CORRECT" -> Value.CORRECT;
                case "INCORRECT" -> Value.INCORRECT;
                default -> Value.NOT_VALIDATED;
            };
            validationsList = diagnosisValidationRepository.findAllByVet_IdAndValue(vet.getId(), valueEnum);
        }

        if (validationsList.isEmpty()) {
            throw new NotFoundException("El veterinario no posee diagnósticos");
        }
        return validationsList.stream().map(
                validation -> mapper.map(validation, DiagnosisValidationDTO.class)
        ).collect(Collectors.toList());
    }

    public List<QuestionDTO> getQuestions() {
        log.info("Obteniendo y devolviendo preguntas desde MongoDB");
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

    public QuestionaryDTO getDiagnosisQuestions(Integer diagnosisId) {
        Optional<Diagnosis> diagnosisOptional = diagnosisRepository.findById(diagnosisId);
        if (diagnosisOptional.isEmpty()) {
            throw new NotFoundException("El diagnóstico no existe");
        }
        Diagnosis diagnosis = diagnosisOptional.get();
        return mapper.map(questionaryRepository.findById(diagnosis.getAnamnesis().getQuestionaryId()), QuestionaryDTO.class);
    }
}
