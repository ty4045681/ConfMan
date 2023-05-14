package com.finale.ConferenceManagement.interfaces;

import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Attendance;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;

import java.util.List;

public interface AttendanceRepositoryCustom {
    long countConferencesByUserAndStatusAndTime(User user, ApplyStatus applyStatus, boolean isConferenceUpcoming);
    long countAttendeesByConferenceAndStatus(Conference conference, ApplyStatus applyStatus);

    List<Attendance> findAttendeesByConferenceAndStatus(Conference conference, ApplyStatus applyStatus);

    User findUserByAttendance(Attendance attendance);
}
