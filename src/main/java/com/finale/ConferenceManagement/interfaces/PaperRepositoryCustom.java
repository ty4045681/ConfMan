package com.finale.ConferenceManagement.interfaces;

import com.finale.ConferenceManagement.model.*;

import java.util.List;

public interface PaperRepositoryCustom {
    long countByAuthorAndStatusAndConferenceTime(User author, ApplyStatus status, boolean isConferenceUpcoming);
    long countByConference(Conference conference);
    List<Paper> findPapersByConference(Conference conference);
}
