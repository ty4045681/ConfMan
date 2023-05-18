package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.dto.GetPapersByUserIdResponse;
import com.finale.ConferenceManagement.exceptions.BadRequestException;
import com.finale.ConferenceManagement.exceptions.UserNotFoundException;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.model.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class JudgeService {
    private UserService userService;
    private ReviewService reviewService;

    public List<GetPapersByUserIdResponse> findPaperByJudgeId(String judgeId) {
        User judge = userService.findById(UUID.fromString(judgeId))
                .orElseThrow(UserNotFoundException::new);

        if (!(judge.getRole() == UserRole.JUDGE)) {
            throw new BadRequestException("User is not a judge");
        }

        return reviewService.findReviewsByJudge(judge)
                .stream()
                .map(review -> new GetPapersByUserIdResponse(reviewService.findPaperByReview(review)))
                .toList();
    }

    public long countPaperByJudgeIdAndStatus(String judgeId, String status) {
        User judge = userService.findById(UUID.fromString(judgeId))
                .orElseThrow(UserNotFoundException::new);

        if (!(judge.getRole() == UserRole.JUDGE)) {
            throw new BadRequestException("User is not a judge");
        }

        try {
            ApplyStatus applyStatus = ApplyStatus.valueOf(status);
            return reviewService.countPaperByJudgeAndStatus(judge, applyStatus);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Status string is illegal");
        }
    }
}
