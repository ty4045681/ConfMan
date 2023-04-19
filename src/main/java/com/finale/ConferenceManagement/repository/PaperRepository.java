package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.model.Paper;
import com.finale.ConferenceManagement.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.UUID;

public interface PaperRepository extends MongoRepository<Paper, UUID> {
    List<Paper> findAllByConferenceId(UUID conferenceId);
    List<Paper> findAllByAuthor(User author);
}
