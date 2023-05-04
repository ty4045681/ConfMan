package com.finale.ConferenceManagement.interfaces;

import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.User;

public interface PaperRepositoryCustom {
    long countByAuthorAndStatusAndConferenceTime(User author, ApplyStatus status, boolean isConferenceUpcoming);
}
