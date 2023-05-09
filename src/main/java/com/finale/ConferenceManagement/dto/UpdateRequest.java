package com.finale.ConferenceManagement.dto;

import lombok.Data;

@Data
public class UpdateRequest {
    private String username;
    private String email;
    private String name;
    private String address;
    private String phoneNumber;
    private String bio;
}
