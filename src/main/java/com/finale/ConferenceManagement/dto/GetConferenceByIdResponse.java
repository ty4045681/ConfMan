package com.finale.ConferenceManagement.dto;

import com.finale.ConferenceManagement.model.Conference;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class GetConferenceByIdResponse {
    private String id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private String theme;
    private String focus;
    private Map<String, String> keynotesAndSpeakers;
    private Map<LocalDateTime, String> agenda;
    private String registrationInfo;
    private List<String> accommodations;
    private LocalDateTime startCallingDateForPapers;
    private LocalDateTime endCallingDateForPapers;
    private LocalDateTime startCallingDateForPresentations;
    private LocalDateTime endCallingDateForPresentations;
    private String guidelineForPaperSubmission;
    private String guidelineForPresentationSubmission;
    private List<String> sponsors;
    private List<String> exhibitors;
    private String phoneNumber;

    public GetConferenceByIdResponse(Conference conference) {
        this.id = conference.getId().toString();
        this.title = conference.getTitle();
        this.startDate = conference.getStartDate();
        this.endDate = conference.getEndDate();
        this.location = conference.getLocation();
        this.theme = conference.getTheme();
        this.focus = conference.getFocus();
        this.keynotesAndSpeakers = conference.getKeynotesAndSpeakers();
        this.agenda = conference.getAgenda();
        this.registrationInfo = conference.getRegistrationInfo();
        this.accommodations = conference.getAccommodations();
        this.startCallingDateForPapers = conference.getStartCallingDateForPapers();
        this.endCallingDateForPapers = conference.getEndCallingDateForPapers();
        this.startCallingDateForPresentations = conference.getStartCallingDateForPresentations();
        this.endCallingDateForPresentations = conference.getEndCallingDateForPresentations();
        this.guidelineForPaperSubmission = conference.getGuidelineForPaperSubmission();
        this.guidelineForPresentationSubmission = conference.getGuidelineForPresentationSubmission();
        this.sponsors = conference.getSponsors();
        this.exhibitors = conference.getExhibitors();
        this.phoneNumber = conference.getPhoneNumber();
    }
}
