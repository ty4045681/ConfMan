package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.repository.ConferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ConferenceService {
    private ConferenceRepository conferenceRepository;
    public long countConferencesByOrganizer(User organizer) {
        return conferenceRepository.countConferencesByOrganizer(organizer);
    }

    public List<Conference> findConferencesByOrganizer(User organizer) {
        return conferenceRepository.findConferencesByOrganizer(organizer);
    }


}
