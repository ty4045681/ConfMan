package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Attendance;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.repository.AttendanceRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final MongoTemplate mongoTemplate;

    public AttendanceService(AttendanceRepository attendanceRepository, MongoTemplate mongoTemplate) {
        this.attendanceRepository = attendanceRepository;
        this.mongoTemplate = mongoTemplate;
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
        return attendanceRepository.countByUserAndStatusAndTime(user, status, isConferenceUpcoming);
    }
}
