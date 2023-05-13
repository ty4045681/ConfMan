package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.dto.GetConferenceByOrganizerIdResponse;
import com.finale.ConferenceManagement.exceptions.BadRequestException;
import com.finale.ConferenceManagement.exceptions.UserNotFoundException;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.model.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrganizerService {
    private UserService userService;
    private ConferenceService conferenceService;
    private PaperService paperService;
    private AttendanceService attendanceService;
    private ReviewService reviewService;

    public long countConferencesByOrganizer(String id) {
        Optional<User> optionalOrganizer = userService.findById(UUID.fromString(id));
        User organizer = optionalOrganizer.orElseThrow(() -> new UserNotFoundException("Organizer not found"));
        if (organizer.getRoles().contains(UserRole.ORGANIZER)) {
            return conferenceService.countConferencesByOrganizer(organizer);
        } else {
            throw new UserNotFoundException("User is not an organizer");
        }
    }

    private List<Conference> findConferencesByOrganizer(String id) {
        Optional<User> optionalOrganizer = userService.findById(UUID.fromString(id));
        User organizer = optionalOrganizer.orElseThrow(() -> new UserNotFoundException("Organizer not found"));
        if (organizer.getRoles().contains(UserRole.ORGANIZER)) {
            return conferenceService.findConferencesByOrganizer(organizer);
        } else {
            throw new UserNotFoundException("User is not an organizer");
        }
    }

    public long countPapersByOrganizer(String id) {
        List<Conference> conferences = findConferencesByOrganizer(id);
        long count = 0;
        for (Conference conference : conferences) {
            count += paperService.countPapersByConference(conference);
        }
        return count;
    }

    public long countAllAttendeesByOrganizer(String id) {
        List<Conference> conferences = findConferencesByOrganizer(id);
        long count = 0;
        for (Conference conference : conferences) {
            count += attendanceService.countAttendeesByConferenceAndStatus(conference, ApplyStatus.APPROVED);
        }
        return count;
    }

    public long countAllJudgesByOrganizer(String id) {
        List<Conference> conferences = findConferencesByOrganizer(id);
//        Set<User> judges = new HashSet<>();
//        for (Conference conference : conferences) {
//            judges.addAll(reviewService.findJudgesByConference(conference));
//        }
//        return judges.size();
        long count = 0;
        for (Conference conference: conferences) {
            count += reviewService.countJudgesByConference(conference);
        }
        return count;
    }

    public List<GetConferenceByOrganizerIdResponse> findConferenceByOrganizerId(String organizerId) {
        Optional<User> optionalUser = userService.findById(UUID.fromString(organizerId));
        if (optionalUser.isPresent()) {
            User organizer = optionalUser.get();

            if (organizer.getRoles().contains(UserRole.ORGANIZER)) {
                List<Conference> conferences = conferenceService.findConferencesByOrganizer(organizer);
                return conferences.stream().map(conference -> new GetConferenceByOrganizerIdResponse(
                        conference.getId().toString(),
                        conference.getTitle(),
                        conference.getStartDate().toString(),
                        conference.getEndDate().toString(),
                        conference.getLocation(),
                        attendanceService.countAttendeesByConferenceAndStatus(conference, ApplyStatus.APPROVED),
                        attendanceService.countAttendeesByConferenceAndStatus(conference, ApplyStatus.REJECTED),
                        attendanceService.countAttendeesByConferenceAndStatus(conference, ApplyStatus.PENDING)
                )).toList();
            } else {
                throw new BadRequestException("User is not an organizer");
            }
        } else {
            throw new UserNotFoundException();
        }
    }
}
