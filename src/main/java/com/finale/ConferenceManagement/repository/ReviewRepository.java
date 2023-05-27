package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.interfaces.ReviewRepositoryCustom;
import com.finale.ConferenceManagement.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends MongoRepository<Review, UUID>, ReviewRepositoryCustom {
    long countJudgesByConference(Conference conference);
    List<User> findJudgesByConference(Conference conference);

    List<Review> findReviewsByConference(Conference conference);

    List<Review> findReviewsByJudge(User judge);
    User findJudgeByReview(Review review);

    Paper findPaperByReview(Review review);

    long countPapersByJudgeAndStatus(User judge, ApplyStatus status);

    void deleteByJudge_Id(UUID judgeId);

    void deleteByPaper_Id(UUID paperId);

    void deleteByConference_Id(UUID conferenceId);
}
