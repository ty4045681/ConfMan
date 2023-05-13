package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.interfaces.AttendanceRepositoryCustom;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Attendance;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, UUID>, AttendanceRepositoryCustom {
    List<Attendance> findAllByUser(@NonNull User user);

//    @Aggregation(pipeline = {
//            "{ $match: { 'user': ?0, 'status': ?1} }",
//            "{ $count: 'count' }"
//    })
//    long countByUserAndStatus(User user, ApplyStatus status);

    long countConferencesByUserAndStatusAndTime(User user, ApplyStatus status, boolean isConferenceUpcoming);

    long countAttendeesByConferenceAndStatus(Conference conference, ApplyStatus applyStatus);
}

