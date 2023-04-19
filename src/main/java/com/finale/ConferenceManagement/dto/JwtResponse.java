package com.finale.ConferenceManagement.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class JwtResponse {
    @NonNull
    private String token;
    private String type = "Bearer";
//    private String id;
//    private String username;
//    private String email;
//    private List<String> roles;
}
