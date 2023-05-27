package com.finale.ConferenceManagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "attendance")
@Data
@AllArgsConstructor
public class Attendance {
    @Id
    private UUID id = UUID.randomUUID();

    @NonNull
    @DBRef
    private User user;

    @NonNull
    @DBRef
    private Conference conference;

    @NonNull
    private ApplyStatus status;

    private String message;
}
