package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.dto.GetAttendeeByOrganizerIdResponse;
import com.finale.ConferenceManagement.dto.GetConferenceByOrganizerIdResponse;
import com.finale.ConferenceManagement.dto.GetJudgeByOrganizerIdResponse;
import com.finale.ConferenceManagement.dto.GetPapersByUserIdResponse;
import com.finale.ConferenceManagement.exceptions.BadRequestException;
import com.finale.ConferenceManagement.exceptions.UserNotFoundException;
import com.finale.ConferenceManagement.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        if (organizer.getRole() == UserRole.ORGANIZER) {
            return conferenceService.countConferencesByOrganizer(organizer);
        } else {
            throw new UserNotFoundException("User is not an organizer");
        }
    }

    private List<Conference> findConferencesByOrganizer(String id) {
        Optional<User> optionalOrganizer = userService.findById(UUID.fromString(id));
        User organizer = optionalOrganizer.orElseThrow(() -> new UserNotFoundException("Organizer not found"));
        if (organizer.getRole() == UserRole.ORGANIZER) {
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

            if (organizer.getRole() == UserRole.ORGANIZER) {
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

    public List<GetAttendeeByOrganizerIdResponse> findAttendeeByOrganizerId(String organizerId) {
        Optional<User> optionalUser = userService.findById(UUID.fromString(organizerId));
        if (optionalUser.isPresent()) {
            User organizer = optionalUser.get();

            if (organizer.getRole() == UserRole.ORGANIZER) {
                List<GetAttendeeByOrganizerIdResponse> responses = new ArrayList<>();
                List<Conference> conferences = conferenceService.findConferencesByOrganizer(organizer);
                for (Conference conference: conferences) {
                    for (ApplyStatus applyStatus: ApplyStatus.values()){
                        List<Attendance> attendances = attendanceService.findAttendeesByConferenceAndStatus(conference, applyStatus);
                        responses.addAll(attendances.stream().map(attendance -> {
                            User user = attendanceService.findUserByAttendance(attendance);
                            return new GetAttendeeByOrganizerIdResponse(
                                    user.getName(),
                                    user.getUsername(),
                                    user.getEmail(),
                                    conference.getTitle(),
                                    applyStatus.name()
                            );
                        }).toList());
                    }
                }

                return responses;
            } else {
                throw new BadRequestException("User is not an organizer");
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    public List<GetPapersByUserIdResponse> findPaperByOrganizerId(String organizerId) {
        User organizer = userService.findById(UUID.fromString(organizerId))
                .orElseThrow(UserNotFoundException::new);

        if (!(organizer.getRole() == UserRole.ORGANIZER)) {
            throw new BadRequestException("User is not an organizer");
        }

        return conferenceService.findConferencesByOrganizer(organizer)
                .stream()
                .flatMap(conference -> paperService.findPapersByConference(conference).stream())
                .map(GetPapersByUserIdResponse::new)
                .collect(Collectors.toList());
    }

    public List<GetJudgeByOrganizerIdResponse> findJudgeByOrganizerId(String organizerId) {
        User organizer = userService.findById(UUID.fromString(organizerId))
                .orElseThrow(UserNotFoundException::new);

        if (!(organizer.getRole() == UserRole.ORGANIZER)) {
            throw new BadRequestException("User is not an organizer");
        }

        return conferenceService.findConferencesByOrganizer(organizer)
                .stream()
                .flatMap(conference -> reviewService.findReviewsByConference(conference).stream()
                        .map(review -> {
                            User judge = reviewService.findJudgeByReview(review);
                            return new GetJudgeByOrganizerIdResponse(
                                    judge.getName(),
                                    judge.getUsername(),
                                    judge.getEmail(),
                                    conference.getTitle()
                            );
                        }))
                .collect(Collectors.toList());
    }
}
