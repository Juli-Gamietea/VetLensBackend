package com.api.vetlens.service;

import com.api.vetlens.dto.diagnosis.DiagnosisRequestDTO;
import com.api.vetlens.dto.diagnosis.DiagnosisResponseDTO;
import com.api.vetlens.dto.questionary.QuestionDTO;
import com.api.vetlens.entity.Anamnesis;
import com.api.vetlens.entity.Diagnosis;
import com.api.vetlens.entity.Dog;
import com.api.vetlens.entity.questionary.Question;
import com.api.vetlens.entity.questionary.Questionary;
import com.api.vetlens.exceptions.NotFoundException;
import com.api.vetlens.repository.DiagnosisRepository;
import com.api.vetlens.repository.DogRepository;
import com.api.vetlens.repository.QuestionRepository;
import com.api.vetlens.repository.QuestionaryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
    private final DogRepository dogRepository;
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

        diagnosis.setAnamnesis(anamnesis);
        diagnosis.setDate(date);
        diagnosis.setDog(dog);
        return mapper.map(diagnosisRepository.save(diagnosis), DiagnosisResponseDTO.class);
    }

    public List<QuestionDTO> getQuestions() {
        List<Question> questions = questionRepository.findAll();
        return questions.stream().map(
                question -> mapper.map(question, QuestionDTO.class)
        ).collect(Collectors.toList());
    }

    public DiagnosisResponseDTO concludeDiagnosis(MultipartFile image, Integer diagnosisId) {
        Optional<Diagnosis> diagnosisOptional = diagnosisRepository.findById(diagnosisId);
        Diagnosis diagnosis = diagnosisOptional.get();
        String imageUrl = cloudinaryService.uploadFile(image, diagnosis.getDog().getOwner().getUsername(), diagnosis.getDog().getName());
        diagnosis.setImageUrl(imageUrl);
        diagnosis.getAnamnesis().setInferences(inferenceService.makeInference(image));
        return mapper.map(diagnosisRepository.save(diagnosis), DiagnosisResponseDTO.class);
    }

    private List<Question> mapQuestionsToEntities(List<QuestionDTO> questionDTOS) {
        return questionDTOS.stream().map(
                questionDTO -> mapper.map(questionDTO, Question.class)
        ).collect(Collectors.toList());
    }
}
