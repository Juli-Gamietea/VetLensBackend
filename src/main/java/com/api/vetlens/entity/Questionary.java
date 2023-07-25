package com.api.vetlens.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Questionary {
    @Id
    private String id;
    @Field
    private String pregunta;
    @Field
    private String respuesta;
}
