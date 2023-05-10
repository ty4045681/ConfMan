package com.finale.ConferenceManagement.controller;

import com.finale.ConferenceManagement.dto.GetConferencesByUserIdResponse;
import com.finale.ConferenceManagement.exceptions.BadRequestException;
import com.finale.ConferenceManagement.exceptions.UserNotFoundException;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.service.AttendanceService;
import com.finale.ConferenceManagement.service.UserService;
import com.finale.ConferenceManagement.util.JwtUtils;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    private JwtUtils jwtUtils;
    private UserService userService;
    private AttendanceService attendanceService;

    @PostMapping("/info")
    public ResponseEntity<?> getCountAttendanceByUserAndConferenceStatus(
            @RequestHeader(value = "Authorization", required = false, defaultValue = "") String token,
            @RequestParam(value = "isConferenceUpcoming", required = false) Boolean isConferenceUpcoming,
            @RequestParam(value = "status", required = false, defaultValue = "") String status
    ) throws BadRequestException, UserNotFoundException {
        // Check if any parameter is null or empty
        if (isConferenceUpcoming == null || status.isEmpty()) {
            throw new BadRequestException("isConferenceUpcoming, and status must not be null or empty");
        }

        if (token.isEmpty()) {
            throw new BadRequestException("Authorization header is missing");
        }

        String jwtToken = jwtUtils.getJwtFromAuthHeader(token);
        String username = jwtUtils.getUsernameFromToken(jwtToken);
        if (jwtToken == null || username == null) {
            throw new BadRequestException("Authorization token is corrupted");
        }

        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        return ResponseEntity.ok(attendanceService.countAttendanceByUserAndConferenceStatus(user, isConferenceUpcoming, ApplyStatus.valueOf(status)));
    }

    @GetMapping("/userId={userId}")
    public ResponseEntity<?> getConferencesByUserId(@PathVariable("userId") String userId) {
        try {
            List<GetConferencesByUserIdResponse> conferences = attendanceService.findAllConferencesByUserId(userId);
            return ResponseEntity.ok().body(conferences);
        } catch (Exception e) {
            throw e;
        }
    }
}
