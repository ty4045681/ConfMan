package com.finale.ConferenceManagement.controller;

import com.finale.ConferenceManagement.model.Paper;
import com.finale.ConferenceManagement.model.Presentation;
import com.finale.ConferenceManagement.service.PaperService;
import com.finale.ConferenceManagement.service.PresentationService;
import com.finale.ConferenceManagement.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class SubmissionController {
    private final PaperService paperService;
    private final PresentationService presentationService;
    private final JwtUtils jwtUtils;

    @PostMapping("/paper/submission")
    public ResponseEntity<?> submitPaper(@RequestHeader("Authorization") String token,
                                         @RequestParam("title") String title,
                                         @RequestParam("authors") String authors,
                                         @RequestParam("abstract") String abstractOfPaper,
                                         @RequestParam("keywords") String keywords,
                                         @RequestParam("file") MultipartFile file,
                                         @RequestParam("conferenceId") String conferenceId) {
        // Validate the paper data
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Title is required");
        }
        if (authors == null || authors.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Authors are required");
        }
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is required");
        }

        String jwtToken = jwtUtils.getJwtFromAuthHeader(token);
        String username = jwtUtils.getUsernameFromToken(jwtToken);

        try {
            Paper paper = paperService.storeFile(
                    conferenceId,
                    username,
                    title,
                    authors,
                    abstractOfPaper,
                    keywords,
                    file
            );

            return new ResponseEntity<>(paper, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/presentation/submission")
    public ResponseEntity<?> submitPresentation(@RequestHeader("Authorization") String token,
                                                @RequestParam("conferenceId") String conferenceId,
                                                @RequestParam("paperId") String paperId,
                                                @RequestParam("title") String title,
                                                @RequestParam("file") MultipartFile file) {
        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Title is required");
        }
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is required");
        }

        String jwtToken = jwtUtils.getJwtFromAuthHeader(token);
        String username = jwtUtils.getUsernameFromToken(jwtToken);

        try {
            Presentation presentation = presentationService.storeFile(
                    conferenceId,
                    paperId,
                    username,
                    title,
                    file
            );

            return new ResponseEntity<>(presentation, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
