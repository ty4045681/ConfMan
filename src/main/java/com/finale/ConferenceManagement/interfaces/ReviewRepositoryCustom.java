package com.finale.ConferenceManagement.interfaces;

import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.Review;
import com.finale.ConferenceManagement.model.User;

import java.util.List;

public interface ReviewRepositoryCustom {
    long countJudgesByConference(Conference conference);
    List<User> findJudgesByConference(Conference conference);

    List<Review> findReviewsByConference(Conference conference);
    User findJudgeByReview(Review review);
}
