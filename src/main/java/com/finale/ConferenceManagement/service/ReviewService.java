package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.model.*;
import com.finale.ConferenceManagement.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public List<Review> findReviewsByJudge(User judge) {
        return reviewRepository.findReviewsByJudge(judge);
    }

    public Paper findPaperByReview(Review review) {
        return reviewRepository.findPaperByReview(review);
    }

    public User findJudgeByReview(Review review) {
        return reviewRepository.findJudgeByReview(review);
    }

    public long countPaperByJudgeAndStatus(User judge, ApplyStatus status) {
        return reviewRepository.countPapersByJudgeAndStatus(judge, status);
    }

    public void deleteByConference_Id(UUID conferenceId) {
        reviewRepository.deleteByConference_Id(conferenceId);
    }

    public void deleteByJudge_Id(UUID judgeId) {
        reviewRepository.deleteByJudge_Id(judgeId);
    }

    public void deleteByPaper_Id(UUID paperId) {
        reviewRepository.deleteByPaper_Id(paperId);
    }
}
