package com.finale.ConferenceManagement.controller;

import com.finale.ConferenceManagement.dto.GetAllConferenceOfUser;
import com.finale.ConferenceManagement.dto.GetAllConferenceResponse;
import com.finale.ConferenceManagement.dto.GetAllConferenceByDate;
import com.finale.ConferenceManagement.dto.GetConferenceByIdResponse;
import com.finale.ConferenceManagement.model.Attendance;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.repository.ConferenceRepository;
import com.finale.ConferenceManagement.service.AttendanceService;
import com.finale.ConferenceManagement.service.UserService;
import com.finale.ConferenceManagement.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/conference")
public class ConferenceController {
    private ConferenceRepository conferenceRepository;
    private JwtUtils jwtUtils;
    private UserService userService;
    private AttendanceService attendanceService;

    private static List<GetAllConferenceOfUser> ensembleConferences(List<Conference> conferenceList,
            List<Attendance> attendanceList) {
        List<GetAllConferenceOfUser> getAllConferenceOfUsers = new ArrayList<>();

        for (int i = 0; i < attendanceList.size(); i++) {
            Attendance attendance = attendanceList.get(i);
            Conference conference = conferenceList.get(i);

            GetAllConferenceOfUser getAllConferenceOfUser = new GetAllConferenceOfUser(conference, attendance);
            getAllConferenceOfUsers.add(getAllConferenceOfUser);
        }

        return getAllConferenceOfUsers;
    }

    @GetMapping
    public ResponseEntity<?> getAllConferences(
            @RequestHeader(value = "Upcoming", required = false) boolean isUpcoming,
            @RequestHeader(value = "Ongoing", required = false) boolean isOngoing,
            @RequestHeader(value = "Past", required = false) boolean isPast) {
        // Count the number of true headers. There should be only one.
        int trueHeaderCount = 0;
        if (isUpcoming)
            trueHeaderCount++;
        if (isOngoing)
            trueHeaderCount++;
        if (isPast)
            trueHeaderCount++;

        if (trueHeaderCount != 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Conference> conferenceList = conferenceRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        if (isUpcoming) {
            conferenceList = conferenceList.stream()
                    .filter(conference -> conference.getStartDate().isAfter(now))
                    .toList();
            return new ResponseEntity<>(
                    conferenceList.stream()
                            .map(GetAllConferenceByDate::new)
                            .collect(Collectors.toSet()),
                    HttpStatus.OK);
        } else if (isOngoing) {
            conferenceList = conferenceList.stream()
                    .filter(conference -> conference.getStartDate().isBefore(now)
                            && conference.getEndDate().isAfter(now))
                    .toList();
            return new ResponseEntity<>(
                    conferenceList.stream()
                            .map(GetAllConferenceByDate::new)
                            .collect(Collectors.toSet()),
                    HttpStatus.OK);
        } else if (isPast) {
            conferenceList = conferenceList.stream()
                    .filter(conference -> conference.getEndDate().isBefore(now))
                    .toList();
            return new ResponseEntity<>(
                    conferenceList.stream()
                            .map(GetAllConferenceByDate::new)
                            .collect(Collectors.toSet()),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id={id}")
    public ResponseEntity<GetConferenceByIdResponse> getConferenceById(@PathVariable("id") String id) {
        UUID uuid = UUID.fromString(id);
        Optional<Conference> conference = conferenceRepository.findById(uuid);
        return conference.map(value -> new ResponseEntity<>(new GetConferenceByIdResponse(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Conference> addConference(@RequestBody Conference conference) {
        Conference savedConference = conferenceRepository.save(conference);
        return new ResponseEntity<>(savedConference, HttpStatus.CREATED);
    }

    @PutMapping("/id={id}")
    public ResponseEntity<Conference> updateConference(@PathVariable("id") String id,
            @RequestBody Conference conference) {
        UUID uuid = UUID.fromString(id);
        Optional<Conference> optionalConference = conferenceRepository.findById(uuid);
        if (optionalConference.isPresent()) {
            conferenceRepository.deleteById(uuid);
        }
        return new ResponseEntity<>(conference, HttpStatus.OK);
    }

    @DeleteMapping("/id={id}")
    public ResponseEntity<Conference> deleteConference(@PathVariable("id") String id) {
        UUID uuid = UUID.fromString(id);
        Optional<Conference> deletedConf = conferenceRepository.findById(uuid);
        if (deletedConf.isPresent()) {
            Conference deletedConfContent = deletedConf.get();
            conferenceRepository.deleteById(uuid);
            // System.out.println(deletedConfContent.getId());
            // TEST NOT PASS. Can't get response body.
            return new ResponseEntity<>(deletedConfContent, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}