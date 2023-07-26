package com.api.vetlens.service;

import com.api.vetlens.dto.DiagnosisRequestDTO;
import com.api.vetlens.dto.MessageDTO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DiagnosisService {
    private final DocumentService documentService;
    private final UserService userService;

    public MessageDTO makeDiagnosis(DiagnosisRequestDTO request){
        MongoDatabase mongoDatabase = documentService.getMongoDatabase();
        MongoCollection<Document> userCollection =  mongoDatabase.getCollection(request.getUsername());
        Document document = new Document("name", "juli");
        userCollection.insertOne(document);
        return new MessageDTO("OK");
    }
}
