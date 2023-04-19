package com.finale.ConferenceManagement.dto;

import java.util.UUID;

public class GetUserByUsernameResponse {
    private String id;
    private String username;

    public GetUserByUsernameResponse(UUID uuid, String username) {
        this.id = uuid.toString();
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id.toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
