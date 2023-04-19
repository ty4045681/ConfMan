package com.finale.ConferenceManagement.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String name;
    private String address;
    private String phoneNumber;
    private String bio;
}
