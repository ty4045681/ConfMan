package com.finale.ConferenceManagement.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.finale.ConferenceManagement.model.Paper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPapersByUserIdResponse {
    private String id;
    private String title;
    private String conferenceTitle;
    private Set<String> authors;
    private Set<String> keywords;
    @JsonProperty("abstract")
    private String abstractOfPaper;
    private String status;

    public GetPapersByUserIdResponse(Paper paper) {
        this(paper.getId().toString(),
                paper.getTitle(),
                paper.getConference().getTitle(),
                paper.getAuthors(),
                paper.getKeywords(),
                paper.getAbstractOfPaper(),
                paper.getStatus().name());
    }
}
