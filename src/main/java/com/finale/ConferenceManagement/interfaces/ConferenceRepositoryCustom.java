package com.finale.ConferenceManagement.interfaces;

import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;

import java.util.List;

public interface ConferenceRepositoryCustom {
    long countConferencesByOrganizer(User organizer);

    List<Conference> findConferencesByOrganizer(User organizer);

}
