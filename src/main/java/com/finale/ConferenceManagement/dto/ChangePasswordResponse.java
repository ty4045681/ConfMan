package com.finale.ConferenceManagement.dto;

import org.jetbrains.annotations.NotNull;

import lombok.Data;

@Data
public class ChangePasswordResponse {
    @NotNull
    private boolean isSuccessful;
}
