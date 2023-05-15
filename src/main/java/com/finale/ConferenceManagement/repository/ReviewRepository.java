package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.interfaces.ReviewRepositoryCustom;
import com.finale.ConferenceManagement.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends MongoRepository<Review, UUID>, ReviewRepositoryCustom {
    long countJudgesByConference(Conference conference);
    List<User> findJudgesByConference(Conference conference);

    List<Review> findReviewsByConference(Conference conference);

    List<Review> findReviewsByJudge(User judge);
    User findJudgeByReview(Review review);

    Paper findPaperByReview(Review review);

    long countPapersByJudgeAndStatus(User judge, ApplyStatus status);
}
