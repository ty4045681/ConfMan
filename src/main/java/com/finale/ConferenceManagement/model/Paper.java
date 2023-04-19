package com.finale.ConferenceManagement.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Document(collection = "paper")
@Data
public class Paper {
    @Id
    private UUID id = UUID.randomUUID();

    // ID of conference this paper belongs to
    @NonNull
    @DBRef
    private Conference conference;

    @NonNull
    @DBRef
    private User author;

    @NonNull
    private ApplyStatus status;

    @NonNull
    private String title;

    @NonNull
    private Set<String> authors;

    @NonNull
    private String abstractOfPaper;

    @NonNull
    private Set<String> keywords;

    @NonNull
    private String fileName;
}
