package com.finale.ConferenceManagement.model;

import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "review")
@Data
public class Review {
    @Id
    private UUID id = UUID.randomUUID();

    @NonNull
    @DBRef
    private User judge;

    @NonNull
    @DBRef
    private Conference conference;

    @DBRef
    private Paper paper;

    private ApplyStatus applyStatus;

    private String message;

    public Review(@NotNull User judge, @NotNull Conference conference, Paper paper, ApplyStatus applyStatus, String message) {
        this.judge = judge;
        this.conference = conference;
        this.paper = paper;
        this.applyStatus = applyStatus;
        this.message = message;
    }
}
