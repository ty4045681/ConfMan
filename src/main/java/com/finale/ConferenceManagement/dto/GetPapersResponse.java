package com.finale.ConferenceManagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Paper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class GetPapersResponse {
    private String id;
    private String conferenceTitle;
    private String title;
    private Set<String> authors;
    @JsonProperty("abstract")
    private String abstractOfPaper;
    private Set<String> keywords;
    private ApplyStatus status;

    public GetPapersResponse(Paper paper) {
        this.id = paper.getId().toString();
        this.conferenceTitle = paper.getConference().getTitle();
        this.title = paper.getTitle();
        this.authors = paper.getAuthors();
        this.abstractOfPaper = paper.getAbstractOfPaper();
        this.keywords = paper.getKeywords();
        this.status = paper.getStatus();
    }
}
