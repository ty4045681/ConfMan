package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.model.Conference;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ConferenceRepository extends MongoRepository<Conference, UUID> {
    Conference findConferenceById(UUID id);
    List<Conference> findByAccommodations(List<String> accommodations);
}

