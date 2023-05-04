package com.finale.ConferenceManagement.interfaces;

import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.User;

public interface AttendanceRepositoryCustom {
    long countByUserAndStatusAndTime(User user, ApplyStatus applyStatus, boolean isConferenceUpcoming);
}
