package com.finale.ConferenceManagement.controller;

import com.finale.ConferenceManagement.dto.GetAllConferenceOfUser;
import com.finale.ConferenceManagement.dto.GetAllConferenceResponse;
import com.finale.ConferenceManagement.dto.GetAllUpcomingConference;
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

    private static List<GetAllConferenceOfUser> ensembleConferences(List<Conference> conferenceList, List<Attendance> attendanceList) {
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
    public ResponseEntity<?> getAllConferences(@RequestHeader(value = "Upcoming", required = false) boolean isUpcoming,
                                               @RequestHeader(value = "Authorization", required = false) String token) {
        List<Conference> conferenceList = conferenceRepository.findAll();

        if (isUpcoming) {
            conferenceList = conferenceList.stream().filter(conference -> conference.getStartDate().isAfter(LocalDateTime.now())).toList();
            return new ResponseEntity<>(conferenceList.stream().map(GetAllUpcomingConference::new).collect(Collectors.toSet()), HttpStatus.OK);
        } else if (token != null) {
              String jwtToken = jwtUtils.getJwtFromAuthHeader(token);
              String username = jwtUtils.getUsernameFromToken(jwtToken);
              Optional<User> user = userService.findByUsername(username);
              return user.map(value -> {
                  List<Attendance> attendanceList = attendanceService.findAllAttendanceByUser(value);
                  List<Conference> conferences = attendanceList.stream().map(Attendance::getConference).toList();
                  List<GetAllConferenceOfUser> getAllConferenceOfUsers = ensembleConferences(conferences, attendanceList);
                  return new ResponseEntity<>(getAllConferenceOfUsers, HttpStatus.OK);
              }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return new ResponseEntity<>(new GetAllConferenceResponse(
                    // get ids from conferences in conferenceList and convert them to List<String>
                    conferenceList.stream().map(conference -> conference.getId().toString()).toList(),
                    // get titles from conferences in conferenceList and convert them to List<String>
                    conferenceList.stream().map(Conference::getTitle).toList()
            ), HttpStatus.OK);
        }
    }

    @GetMapping("/id={id}")
    public ResponseEntity<GetConferenceByIdResponse> getConferenceById(@PathVariable("id") String id) {
        UUID uuid = UUID.fromString(id);
        Optional<Conference> conference = conferenceRepository.findById(uuid);
        return conference.map(value -> new ResponseEntity<>(new GetConferenceByIdResponse(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Conference> addConference(@RequestBody Conference conference) {
        Conference savedConference = conferenceRepository.save(conference);
        return new ResponseEntity<>(savedConference, HttpStatus.CREATED);
    }

    @PutMapping("/id={id}")
    public ResponseEntity<Conference> updateConference(@PathVariable("id") String id, @RequestBody Conference conference) {
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
//            System.out.println(deletedConfContent.getId());
            // TEST NOT PASS. Can't get response body.
            return new ResponseEntity<>(deletedConfContent, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}