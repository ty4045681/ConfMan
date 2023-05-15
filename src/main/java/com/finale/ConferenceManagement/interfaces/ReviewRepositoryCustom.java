package com.finale.ConferenceManagement.interfaces;

import com.finale.ConferenceManagement.model.*;

import java.util.List;

public interface ReviewRepositoryCustom {
    long countJudgesByConference(Conference conference);
    List<User> findJudgesByConference(Conference conference);

    List<Review> findReviewsByConference(Conference conference);
    List<Review> findReviewsByJudge(User judge);
    User findJudgeByReview(Review review);
    Paper findPaperByReview(Review review);

    long countPapersByJudgeAndStatus(User judge, ApplyStatus status);
}
