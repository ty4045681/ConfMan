package com.finale.ConferenceManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

@AllArgsConstructor
public class GetJudgeByOrganizerIdResponse {
    private String name;
    private String username;
    private String email;
    private String conferenceTitle;
}
