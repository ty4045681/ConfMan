package com.finale.ConferenceManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetConferenceByOrganizerIdResponse {
    private String id;
    private String title;
    private String startDate;
    private String endDate;
    private String location;
    private long acceptedUsersNumber;
    private long rejectedUsersNumber;
    private long pendingUsersNumber;
    private String status;
}
