package com.finale.ConferenceManagement.dto;

import com.finale.ConferenceManagement.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserByAdminIdResponse {
    private String name;
    private String username;
    private String email;
    private String userType;

    public GetUserByAdminIdResponse(User user) {
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.userType = user.getRole().toString();
    }
}
