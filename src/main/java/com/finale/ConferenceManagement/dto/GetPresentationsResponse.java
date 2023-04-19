package com.finale.ConferenceManagement.dto;

import com.finale.ConferenceManagement.model.Presentation;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPresentationsResponse {
    private String id;
    private String conferenceId;
    private String paperId;
    private String title;

    public GetPresentationsResponse(Presentation presentation) {
        this.id = presentation.getId().toString();
        this.conferenceId = presentation.getConference().getId().toString();
        this.paperId = presentation.getPaper().getId().toString();
        this.title = presentation.getTitle();
    }
}
