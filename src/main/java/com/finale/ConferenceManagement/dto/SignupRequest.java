package com.finale.ConferenceManagement.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private String role;
    private String name;
}
