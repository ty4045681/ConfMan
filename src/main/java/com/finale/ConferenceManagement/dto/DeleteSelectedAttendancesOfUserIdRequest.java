package com.finale.ConferenceManagement.dto;

import java.util.List;

import lombok.Data;

@Data
public class DeleteSelectedAttendancesOfUserIdRequest {
    private List<String> ids;
}