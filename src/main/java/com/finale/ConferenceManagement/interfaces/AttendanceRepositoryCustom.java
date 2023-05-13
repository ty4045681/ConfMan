package com.finale.ConferenceManagement.interfaces;

import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;

public interface AttendanceRepositoryCustom {
    long countConferencesByUserAndStatusAndTime(User user, ApplyStatus applyStatus, boolean isConferenceUpcoming);
    long countAttendeesByConferenceAndStatus(Conference conference, ApplyStatus applyStatus);
}
