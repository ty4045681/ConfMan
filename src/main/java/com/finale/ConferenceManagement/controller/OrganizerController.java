package com.finale.ConferenceManagement.controller;

import com.finale.ConferenceManagement.service.OrganizerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/organizer")
public class OrganizerController {
    private OrganizerService organizerService;

    @GetMapping("/id={id}/conference/count")
    public ResponseEntity<?> getCountConferencesByOrganizer(@PathVariable("id") String id) {
        return ResponseEntity.ok(organizerService.countConferencesByOrganizer(id));
    }

    @GetMapping("/id={id}/paper/count")
    public ResponseEntity<?> getCountPapersByOrganizer(@PathVariable("id") String id) {
        return ResponseEntity.ok(organizerService.countPapersByOrganizer(id));
    }

    @GetMapping("/id={id}/attendees/count=all")
    public ResponseEntity<?> getCountAllAttendeesByOrganizer(@PathVariable("id") String id) {
        return ResponseEntity.ok(organizerService.countAllAttendeesByOrganizer(id));
    }

    @GetMapping("/id={id}/reviewers/count=all")
    public ResponseEntity<?> getCountAllReviewersByOrganizer(@PathVariable("id") String id) {
        return ResponseEntity.ok(organizerService.countAllJudgesByOrganizer(id));
    }

    @GetMapping("/organizerId={id}")
    public ResponseEntity<?> getConferencesByOrganizerId(@PathVariable("id") String id) {
        return ResponseEntity.ok(organizerService.findConferenceByOrganizerId(id));
    }
}
