package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.repository.ConferenceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConferenceService {
    private ConferenceRepository conferenceRepository;
    private AttendanceService attendanceService;
    private PaperService paperService;
    private ReviewService reviewService;

    public long countConferencesByOrganizer(User organizer) {
        return conferenceRepository.countConferencesByOrganizer(organizer);
    }

    public long countConferencesByStatus(ApplyStatus status) {
        return conferenceRepository.countConferencesByStatus(status);
    }

    public List<Conference> findConferencesByOrganizer(User organizer) {
        return conferenceRepository.findConferencesByOrganizer(organizer);
    }

    // used by admin
    public List<Conference> findAllConferences() {
        return conferenceRepository.findAll();
    }

    public Optional<Conference> findConferencesById(UUID id) {
        return conferenceRepository.findConferenceById(id);
    }

    public void deleteConference(Conference conference) {
        conferenceRepository.delete(conference);
    }

    public void deleteByOrganizer_Id(UUID organizerId) {
        conferenceRepository.deleteByOrganizer_Id(organizerId);
    }

    @Transactional
    public void cascadingDeleteConference(UUID id) {
        conferenceRepository.deleteById(id);
        attendanceService.deleteByConference_Id(id);
        paperService.deleteByConference_Id(id);
        reviewService.deleteByConference_Id(id);
    }
}
