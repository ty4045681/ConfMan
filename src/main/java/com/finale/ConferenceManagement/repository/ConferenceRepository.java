package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.model.Conference;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ConferenceRepository extends MongoRepository<Conference, UUID> {
    Conference findConferenceById(UUID id);
}

