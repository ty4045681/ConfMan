package com.finale.ConferenceManagement.controller;

import com.finale.ConferenceManagement.dto.GetPapersResponse;
import com.finale.ConferenceManagement.dto.GetPresentationsResponse;
import com.finale.ConferenceManagement.model.Paper;
import com.finale.ConferenceManagement.model.Presentation;
import com.finale.ConferenceManagement.service.PaperService;
import com.finale.ConferenceManagement.service.PresentationService;
import com.finale.ConferenceManagement.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class DownloadController {
    private PaperService paperService;
    private PresentationService presentationService;
    private JwtUtils jwtUtils;

    @GetMapping("/paper")
    public ResponseEntity<Set<GetPapersResponse>> getPapers(@RequestParam(value = "conferenceId", required = false) String conferenceId,
                                                            @RequestHeader(value = "Authorization", required = false) String token) {
        List<Paper> papers;

        if (conferenceId != null) {
            papers = paperService.findAllPapersByConferenceId(conferenceId);
        } else if (token != null) {
            String jwtToken = jwtUtils.getJwtFromAuthHeader(token);
            String username = jwtUtils.getUsernameFromToken(jwtToken);
            papers = paperService.findAllPapersByUsername(username);
        } else {
            papers = paperService.findAllPapers();
        }

        return ResponseEntity.ok().body(papers.stream().map(GetPapersResponse::new).collect(Collectors.toSet()));
    }

    @GetMapping("/presentation")
    public ResponseEntity<Set<GetPresentationsResponse>> getPresentations(@RequestParam(value = "conferenceId", required = false) String conferenceId,
                                                                          @RequestParam(value = "paperId", required = false) String paperId) {
        List<Presentation> presentations;

        if (conferenceId != null && paperId != null) {
            presentations = presentationService.findAllPresentationsByConferenceIdAndPaperId(UUID.fromString(conferenceId), UUID.fromString(paperId));
        } else if (conferenceId != null) {
            presentations = presentationService.findAllPresentationsByConferenceId(UUID.fromString(conferenceId));
        } else if (paperId != null) {
            presentations = presentationService.findAllPresentationsByPaperId(UUID.fromString(paperId));
        } else {
            presentations = presentationService.findAllPresentations();
        }

        return ResponseEntity.ok().body(presentations.stream().map(GetPresentationsResponse::new).collect(Collectors.toSet()));
    }

    @GetMapping("/paper/{id}/download")
    public ResponseEntity<?> downloadPaper(@PathVariable String id) {
        Optional<Paper> paperOptional = paperService.findPaperById(UUID.fromString(id));

        if (paperOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Paper paper = paperOptional.get();
        Resource resource = paperService.loadFileAsResource(paper.getFileName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + paper.getFileName() + "\"")
                .body(resource);
    }

    @GetMapping("/presentation/{id}/download")
    public ResponseEntity<?> downloadPresentation(@PathVariable String id) {
        Optional<Presentation> presentationOptional = presentationService.findPresentationById(UUID.fromString(id));

        if (presentationOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Presentation presentation = presentationOptional.get();
        Resource resource = presentationService.loadFileAsResource(presentation.getFileName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + presentation.getFileName() + "\"")
                .body(resource);
    }

}