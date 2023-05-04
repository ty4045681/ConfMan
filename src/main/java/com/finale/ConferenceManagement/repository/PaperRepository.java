package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.interfaces.PaperRepositoryCustom;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Paper;
import com.finale.ConferenceManagement.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface PaperRepository extends MongoRepository<Paper, UUID>, PaperRepositoryCustom {
    List<Paper> findAllByConferenceId(UUID conferenceId);
    List<Paper> findAllByAuthor(User author);

    long countByAuthor(User author);

    long countByAuthorAndStatus(User author, ApplyStatus status);

    long countByAuthorAndStatusAndConferenceTime(User author, ApplyStatus status, boolean isConferenceUpcoming);
}
