package com.finale.ConferenceManagement.dto;

public class GetUserByIdResponse {
    private String username;

    public GetUserByIdResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
