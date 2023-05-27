package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.model.Presentation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface PresentationRepository extends MongoRepository<Presentation, UUID> {
    List<Presentation> findAllByConferenceId(UUID conferenceId);
    List<Presentation> findAllByPaperId(UUID paperId);
    List<Presentation> findAllByConferenceIdAndPaperId(UUID conferenceId, UUID paperId);
}
