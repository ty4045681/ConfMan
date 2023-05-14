package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.Review;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public long countJudgesByConference(Conference conference) {
        return reviewRepository.countJudgesByConference(conference);
    }

//    public List<User> findJudgesByConference(Conference conference) {
//        return reviewRepository.findJudgesByConference(conference);
//    }

    public List<Review> findReviewsByConference(Conference conference) {
        return reviewRepository.findReviewsByConference(conference);
    }

    public User findJudgeByReview(Review review) {
        return reviewRepository.findJudgeByReview(review);
    }
}
