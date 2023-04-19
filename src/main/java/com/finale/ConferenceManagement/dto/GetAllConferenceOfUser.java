package com.finale.ConferenceManagement.dto;

import com.finale.ConferenceManagement.model.Attendance;
import com.finale.ConferenceManagement.model.Conference;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAllConferenceOfUser {
    private String id;
    private String title;
    private String startDate;
    private String endDate;
    private String status;

    public GetAllConferenceOfUser(Conference conference, Attendance attendance) {
        this.id = conference.getId().toString();
        this.title = conference.getTitle();
        this.startDate = conference.getStartDate().toString();
        this.endDate = conference.getEndDate().toString();
        this.status = attendance.getStatus().name();
    }
}
