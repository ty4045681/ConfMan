package com.finale.ConferenceManagement.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Document(collection = "user")
@Data
public class User {
    // Identifier
    @Id
    private UUID id = UUID.randomUUID();

    // 1. User account information
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private Set<UserRole> roles;

    // 2. Personal information
    @NonNull
    private String name;
    private String address;
    private String phoneNumber;
    private String bio;

    // 3. Conference attendance history
    @NonNull
    private Set<UUID> pastAttendedConference;
}
