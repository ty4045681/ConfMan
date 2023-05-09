package com.finale.ConferenceManagement.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String id;
    private String currentPassword;
    private String newPassword;
}
