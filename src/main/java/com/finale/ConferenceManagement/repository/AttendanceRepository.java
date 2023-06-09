package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.interfaces.AttendanceRepositoryCustom;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Attendance;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface AttendanceRepository extends MongoRepository<Attendance, UUID>, AttendanceRepositoryCustom {
    List<Attendance> findAllByUser(@NonNull User user);

//    @Aggregation(pipeline = {
//            "{ $match: { 'user': ?0, 'status': ?1} }",
//            "{ $count: 'count' }"
//    })
//    long countByUserAndStatus(User user, ApplyStatus status);

    long countConferencesByUserAndStatusAndTime(User user, ApplyStatus status, boolean isConferenceUpcoming);

    long countAttendeesByConferenceAndStatus(Conference conference, ApplyStatus applyStatus);

    List<Attendance> findAttendeesByConferenceAndStatus(Conference conference, ApplyStatus applyStatus);

    User findUserByAttendance(Attendance attendance);

    void deleteByUser_Id(UUID userId);

    void deleteByConference_Id(UUID conferenceId);
}

