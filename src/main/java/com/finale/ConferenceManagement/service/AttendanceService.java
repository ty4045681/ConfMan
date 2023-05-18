package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.dto.GetConferencesByUserIdResponse;
import com.finale.ConferenceManagement.exceptions.UserNotFoundException;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Attendance;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.repository.AttendanceRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserService userService;
    public AttendanceService(AttendanceRepository attendanceRepository, UserService userService,MongoTemplate mongoTemplate) {
        this.attendanceRepository = attendanceRepository;
        this.userService = userService;
    }

    public Attendance create(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }

    public Attendance findById(UUID id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    public Attendance update(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public void delete(UUID id) {
        attendanceRepository.deleteById(id);
    }

    public List<Attendance> findAllAttendanceByUser(User user) {
        return attendanceRepository.findAllByUser(user);
    }

    public List<GetConferencesByUserIdResponse> findAllConferencesByUserId(String userId) {
        Optional<User> optionalUser = userService.findById(UUID.fromString(userId));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            List<Attendance> attendances = findAllAttendanceByUser(user);
            List<Conference> conferences = attendances.stream().map(Attendance::getConference).toList();

            List<GetConferencesByUserIdResponse> responses = new ArrayList<>();
            for (int i = 0; i < attendances.size(); i++) {
                responses.add(new GetConferencesByUserIdResponse(conferences.get(i), attendances.get(i)));
            }

            return responses;
        } else {
            throw new UserNotFoundException();
        }
    }

    /**
     * Count a user's attendance by status and the time of conferences.
     * Whether the conference is upcoming or not is determined by the current time.
     * If the startDate of conference is after the current time, the conference is upcoming.
     * @param user
     * @param isConferenceUpcoming
     * @param status
     * @return
     *  the number of user's attendance by status and the time of conferences
     */
    public long countAttendanceByUserAndConferenceStatus(@NotNull User user, boolean isConferenceUpcoming, ApplyStatus status) {
        return attendanceRepository.countConferencesByUserAndStatusAndTime(user, status, isConferenceUpcoming);
    }

    public long countAttendeesByConferenceAndStatus(Conference conference, ApplyStatus applyStatus) {
        return attendanceRepository.countAttendeesByConferenceAndStatus(conference, applyStatus);
    }

    public List<Attendance> findAttendeesByConferenceAndStatus(Conference conference, ApplyStatus applyStatus) {
        return attendanceRepository.findAttendeesByConferenceAndStatus(conference, applyStatus);
    }

    public User findUserByAttendance(Attendance attendance) {
        return attendanceRepository.findUserByAttendance(attendance);
    }

    public void deleteSelectedAttendancesOfUserId(String userId, List<String> attendanceIds) {
        Optional<User> optionalUser = userService.findById(UUID.fromString(userId));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            List<Attendance> attendances = findAllAttendanceByUser(user);
            List<Attendance> selectedAttendances = attendances.stream()
                    .filter(attendance -> attendanceIds.contains(attendance.getId().toString()))
                    .toList();

            attendanceRepository.deleteAll(selectedAttendances);
        } else {
            throw new UserNotFoundException();
        }
    }
}
