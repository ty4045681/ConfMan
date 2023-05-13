package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.interfaces.ReviewRepositoryCustom;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.Review;
import com.finale.ConferenceManagement.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ReviewRepository extends MongoRepository<Review, UUID>, ReviewRepositoryCustom {
    long countJudgesByConference(Conference conference);
    Set<User> findJudgesByConference(Conference conference);
}
