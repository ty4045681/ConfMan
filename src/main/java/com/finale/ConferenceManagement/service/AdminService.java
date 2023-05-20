package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.dto.GetConferencesByAdminIdResponse;
import com.finale.ConferenceManagement.dto.GetPapersResponse;
import com.finale.ConferenceManagement.dto.GetUserByAdminIdResponse;
import com.finale.ConferenceManagement.exceptions.BadRequestException;
import com.finale.ConferenceManagement.exceptions.UserNotFoundException;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.model.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.swing.text.html.Option;

@Service
@AllArgsConstructor
public class AdminService {
    private ConferenceService conferenceService;
    private PaperService paperService;
    private UserService userService;

    public long countConferencesByStatus(String status) {
        try {
            ApplyStatus applyStatus = ApplyStatus.valueOf(status.toUpperCase());
            return conferenceService.countConferencesByStatus(applyStatus);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status");
        }
    }

    public long countUsersByType(String type) {
        try {
            UserRole role = UserRole.valueOf(type.toUpperCase());
            return userService.countUsersByRole(role);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid user type");
        }
    }

    public List<GetConferencesByAdminIdResponse> findConferencesByAdminId(String id) {
        User admin = userService.findById(UUID.fromString(id))
                .orElseThrow(UserNotFoundException::new);
        if (admin.getRole() != UserRole.ADMIN) {
            throw new BadRequestException("User is not admin");
        }

        return conferenceService.findAllConferences().stream().map(conference -> {
            User organizer = userService.findById(conference.getOrganizer().getId())
                    .orElseThrow(() -> new UserNotFoundException("Organizer is not found"));

            return new GetConferencesByAdminIdResponse(
                    conference.getId().toString(),
                    conference.getTitle(),
                    conference.getStartDate().toString(),
                    conference.getEndDate().toString(),
                    conference.getLocation(),
                    organizer.getUsername(),
                    conference.getStatus().name()
            );
        }).toList();
    }

    public List<GetPapersResponse> findPapersByAdminId(String id) {
        User admin = userService.findById(UUID.fromString(id))
                .orElseThrow(UserNotFoundException::new);
        if (admin.getRole() != UserRole.ADMIN) {
            throw new BadRequestException("User is not admin");
        }

        return paperService.findAllPapers().stream().map(GetPapersResponse::new).toList();
    }

    public List<GetUserByAdminIdResponse> findUsersByAdminId(String id) {
        User admin = userService.findById(UUID.fromString(id))
                .orElseThrow(UserNotFoundException::new);
        if (admin.getRole() != UserRole.ADMIN) {
            throw new BadRequestException("User is not admin");
        }

        return userService.findAllUsers().stream().map(GetUserByAdminIdResponse::new).toList();
    }

    public void deleteSelectedConferencesByAdminId(String id, List<String> conferenceIds) {
        User admin = userService.findById(UUID.fromString(id))
                .orElseThrow(UserNotFoundException::new);
        if (admin.getRole() != UserRole.ADMIN) {
            throw new BadRequestException("User is not admin");
        }

        for (String conferenceId : conferenceIds) {
            Optional<Conference> optionalConference = conferenceService.findConferencesById(UUID.fromString(conferenceId));
            if (optionalConference.isPresent()) {
                Conference conference = optionalConference.get();
                conferenceService.cascadingDeleteConference(conference.getId());
            } else {
                throw new BadRequestException("Conference is not found");
            }
        }
    }

    public void deleteSelectedUsersByAdminId(String id, List<String> userIds) {
        User admin = userService.findById(UUID.fromString(id))
                .orElseThrow(UserNotFoundException::new);
        if (admin.getRole() != UserRole.ADMIN) {
            throw new BadRequestException("User is not admin");
        }

        for (String userId : userIds) {
            Optional<User> optionalUser = userService.findById(UUID.fromString(userId));
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                userService.cascadingDeleteUser(user.getId());
            } else {
                throw new BadRequestException("User is not found");
            }
        }
    }
}
