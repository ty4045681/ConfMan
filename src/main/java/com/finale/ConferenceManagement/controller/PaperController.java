package com.finale.ConferenceManagement.controller;

import com.finale.ConferenceManagement.dto.GetPapersByUserIdResponse;
import com.finale.ConferenceManagement.exceptions.BadRequestException;
import com.finale.ConferenceManagement.exceptions.UserNotFoundException;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Paper;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.service.PaperService;
import com.finale.ConferenceManagement.service.UserService;
import com.finale.ConferenceManagement.util.JwtUtils;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/paper")
public class PaperController {
    private final PaperService paperService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/info")
    public ResponseEntity<?> getCountPaperByUserAndConferenceTimeAndStatus(
            @RequestHeader(value = "Authorization", required = false, defaultValue = "") String token,
            @RequestParam(value = "isConferenceUpcoming", required = false) Boolean isConferenceUpcoming,
            @RequestParam(value = "status", required = false, defaultValue = "") String status
    ) throws BadRequestException, UserNotFoundException {
        if (token.isEmpty()) {
            throw new BadRequestException("Authorization header is missing");
        }

        if (isConferenceUpcoming == null || status.isEmpty()) {
            throw new BadRequestException("isConferenceUpcoming, and status must not be null or empty");
        }

        String jwtToken = jwtUtils.getJwtFromAuthHeader(token);
        String username = jwtUtils.getUsernameFromToken(jwtToken);
        if (jwtToken == null || username == null) {
            throw new BadRequestException("Authorization token is corrupted");
        }

        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }

        return ResponseEntity.ok(paperService.countPaperByAuthorAndStatusAndConferenceTime(username, ApplyStatus.valueOf(status), isConferenceUpcoming));
    }

    @GetMapping("/userId={userId}")
    public ResponseEntity<?> getPapersByUserId(@PathVariable("userId") String userId) {
        try {
            List<Paper> papers = paperService.findAllPapersByUserId(userId);
            List<GetPapersByUserIdResponse> responses = papers.stream().map(paper -> new GetPapersByUserIdResponse(paper)).toList();
            return ResponseEntity.ok().body(responses);
        } catch (Exception e) {
            throw e;
        }
    }
}
