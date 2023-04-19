package com.finale.ConferenceManagement.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "presentation")
@Data
public class Presentation {
    @Id
    private UUID id = UUID.randomUUID();

    @DBRef
    @NonNull
    private Conference conference;

    @DBRef
    @NonNull
    private User author;

    @NonNull
    private ApplyStatus status;

    @NonNull
    @DBRef
    private Paper paper;

    @NonNull
    private String title;

    @NonNull
    private String fileName;
}
