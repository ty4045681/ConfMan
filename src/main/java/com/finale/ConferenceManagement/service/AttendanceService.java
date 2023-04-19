package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Attendance;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AttendanceService {
    private AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
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
}
