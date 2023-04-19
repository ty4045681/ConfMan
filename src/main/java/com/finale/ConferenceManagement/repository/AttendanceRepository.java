package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.model.Attendance;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface AttendanceRepository extends MongoRepository<Attendance, UUID> {
    List<Attendance> findAllByUser(@NonNull User user);
}
