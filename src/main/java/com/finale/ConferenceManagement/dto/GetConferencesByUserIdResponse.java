package com.finale.ConferenceManagement.dto;

import com.finale.ConferenceManagement.model.Attendance;
import com.finale.ConferenceManagement.model.Conference;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetConferencesByUserIdResponse {
    private String id;
    private String title;
    private String startDate;
    private String endDate;
    private String location;
    private String status;

    public GetConferencesByUserIdResponse(Conference conference, Attendance attendance) {
        this(conference.getId().toString(), conference.getTitle(), conference.getStartDate().toString(),
                conference.getEndDate().toString(), conference.getLocation(), attendance.getStatus().name());
    }
}
