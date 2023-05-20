package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.interfaces.ConferenceRepositoryCustom;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConferenceRepository extends MongoRepository<Conference, UUID>, ConferenceRepositoryCustom {
    Optional<Conference> findConferenceById(UUID id);
    List<Conference> findByAccommodations(List<String> accommodations);

    long countConferencesByOrganizer(User organizer);

    long countConferencesByStatus(ApplyStatus status);

    List<Conference> findConferencesByOrganizer(User organizer);

    void deleteByOrganizer_Id(UUID organizerId);

}

