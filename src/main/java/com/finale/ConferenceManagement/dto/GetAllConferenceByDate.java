package com.finale.ConferenceManagement.dto;

import com.finale.ConferenceManagement.model.Conference;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAllConferenceByDate {
    private String id;
    private String title;
    private String startDate;
    private String endDate;
    private String location;
    private String theme;
    private String focus;

    public GetAllConferenceByDate(Conference conference) {
        this.id = conference.getId().toString();
        this.title = conference.getTitle();
        this.startDate = conference.getStartDate().toString();
        this.endDate = conference.getEndDate().toString();
        this.location = conference.getLocation();
        this.theme = conference.getTheme();
        this.focus = conference.getFocus();
    }
}
