package com.finale.ConferenceManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetConferencesByAdminIdResponse {
    private String id;
    private String title;
    private String startDate;
    private String endDate;
    private String location;
    private String organizer;
    private String status;
}
