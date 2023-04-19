package com.finale.ConferenceManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetAllConferenceResponse {
    private List<String> id;
    private List<String> title;
}
